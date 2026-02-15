import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { userApi } from '../utils/userApi';
import { register } from '../utils/authApi';
import { canCreateUser, canDeleteUser, canUpdateUserRole, canViewUsers } from '../utils/rbac';
import { decodeJWT } from '../utils/jwt';
import { UserRole } from '../types';

const ROLES: UserRole[] = ['USER', 'ADMIN', 'SUPER_ADMIN'];

export const UsersPage: React.FC = () => {
  const { role } = useAuth();
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currentUserIdentifier, setCurrentUserIdentifier] = useState<string | null>(null);

  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [createUsername, setCreateUsername] = useState('');
  const [createPassword, setCreatePassword] = useState('');
  const [createEmail, setCreateEmail] = useState('');
  const [createRole, setCreateRole] = useState<UserRole>('USER');
  const [createError, setCreateError] = useState('');
  const [createSubmitting, setCreateSubmitting] = useState(false);

  const [updateRoleUser, setUpdateRoleUser] = useState<any | null>(null);
  const [updateRoleValue, setUpdateRoleValue] = useState<UserRole>('USER');
  const [updateRoleSubmitting, setUpdateRoleSubmitting] = useState(false);
  const [updateRoleError, setUpdateRoleError] = useState('');

  const hasPermission = canViewUsers(role);
  const showCreateUser = canCreateUser(role);
  const showUpdateRole = canUpdateUserRole(role);

  const loadUsers = useCallback(async () => {
    if (!hasPermission) {
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const data = await userApi.getAll();

      // Ensure we always store an array for rendering.
      if (Array.isArray(data)) {
        setUsers(data);
      } else if (data && Array.isArray((data as any).items)) {
        setUsers((data as any).items);
      } else if (data && Array.isArray((data as any).content)) {
        setUsers((data as any).content);
      } else {
        setUsers([]);
      }
    } catch (err) {
      console.error('Failed to load users', err);
      setError('Failed to load users. Please try again.');
    } finally {
      setLoading(false);
    }
  }, [hasPermission]);

  useEffect(() => {
    // Determine the current logged-in user identifier (e.g., username/email/id) from the JWT.
    try {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('jwtToken');
        if (token) {
          const decoded = decodeJWT(token);
          if (decoded?.sub) {
            setCurrentUserIdentifier(String(decoded.sub));
          }
        }
      }
    } catch (e) {
      console.error('Failed to determine current user from token', e);
    }

    loadUsers();
  }, [loadUsers]);

  const isCurrentUser = useCallback(
    (user: any): boolean => {
      if (!currentUserIdentifier || !user) return false;
      const idMatches =
        user.id != null && String(user.id) === String(currentUserIdentifier);
      const usernameMatches =
        user.username != null && String(user.username) === String(currentUserIdentifier);
      const emailMatches =
        user.email != null && String(user.email) === String(currentUserIdentifier);
      return idMatches || usernameMatches || emailMatches;
    },
    [currentUserIdentifier]
  );

  const handleDelete = async (user: any) => {
    if (!canDeleteUser(role)) {
      alert('You do not have permission to delete users.');
      return;
    }

    if (isCurrentUser(user)) {
      alert('You cannot delete your own account.');
      return;
    }

    const userId =
      (user && (user.id || user.userId || user.username || user.email)) ?? null;

    if (!userId) {
      alert('Cannot delete this user because no identifier was found.');
      return;
    }

    if (!confirm('Delete this user?')) return;

    try {
      setLoading(true);
      await userApi.remove(String(userId));
      await loadUsers();
    } catch (err) {
      console.error('Failed to delete user', err);
      setError('Failed to delete user. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUserOpen = () => {
    setCreateUsername('');
    setCreatePassword('');
    setCreateEmail('');
    setCreateRole('USER');
    setCreateError('');
    setCreateModalOpen(true);
  };

  const handleCreateUserSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setCreateError('');
    setCreateSubmitting(true);
    try {
      const result = await register({
        username: createUsername,
        password: createPassword,
        email: createEmail,
        role: createRole,
      });
      if (result.ok) {
        setCreateModalOpen(false);
        await loadUsers();
      } else {
        setCreateError(result.message ?? 'Failed to create user');
      }
    } finally {
      setCreateSubmitting(false);
    }
  };

  const handleUpdateRoleOpen = (user: any) => {
    const currentRole = (user?.role ?? 'USER') as UserRole;
    setUpdateRoleUser(user);
    setUpdateRoleValue(ROLES.includes(currentRole) ? currentRole : 'USER');
    setUpdateRoleError('');
    setUpdateRoleSubmitting(false);
  };

  const handleUpdateRoleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!updateRoleUser) return;
    const userId = updateRoleUser.id ?? updateRoleUser.userId;
    if (userId == null) return;
    setUpdateRoleError('');
    setUpdateRoleSubmitting(true);
    try {
      await userApi.updateRole(String(userId), updateRoleValue);
      setUpdateRoleUser(null);
      await loadUsers();
    } catch (err) {
      setUpdateRoleError(err instanceof Error ? err.message : 'Failed to update role');
    } finally {
      setUpdateRoleSubmitting(false);
    }
  };

  const columns = useMemo(() => {
    if (!users.length || typeof users[0] !== 'object' || users[0] === null) {
      return [] as string[];
    }

    const hiddenColumns = ['canDelete'];

    return Object.keys(users[0] as Record<string, unknown>).filter(
      (key) => !hiddenColumns.includes(key)
    );
  }, [users]);

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-semibold text-gray-900">Users</h2>
        {showCreateUser && (
          <button
            type="button"
            onClick={handleCreateUserOpen}
            className="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded hover:bg-blue-700 transition-colors"
          >
            Create User
          </button>
        )}
      </div>

      {!hasPermission ? (
        <div className="text-center py-12 bg-white rounded-lg border border-gray-200">
          <p className="text-gray-600">
            You do not have permission to view users.
          </p>
        </div>
      ) : (
        <div className="bg-white rounded-lg border border-gray-200">
          {loading ? (
            <div className="text-center py-12">
              <p className="text-gray-600">Loading users...</p>
            </div>
          ) : error ? (
            <div className="text-center py-12">
              <p className="text-red-600">{error}</p>
            </div>
          ) : users.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-600">No users found.</p>
            </div>
          ) : columns.length === 0 ? (
            <div className="p-4">
              <pre className="text-sm text-gray-800 overflow-auto">
                {JSON.stringify(users, null, 2)}
              </pre>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    {columns.map((column) => (
                      <th
                        key={column}
                        scope="col"
                        className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        {column}
                      </th>
                    ))}
                    {(canDeleteUser(role) || showUpdateRole) && (
                      <th
                        scope="col"
                        className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Actions
                      </th>
                    )}
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {users.map((user, index) => (
                    <tr key={index}>
                      {columns.map((column) => (
                        <td key={column} className="px-4 py-2 text-sm text-gray-900">
                          {String((user as any)[column] ?? '')}
                        </td>
                      ))}
                      {(canDeleteUser(role) || showUpdateRole) && (
                        <td className="px-4 py-2 text-sm space-x-2">
                          {showUpdateRole && (
                            isCurrentUser(user) ? (
                              <button
                                type="button"
                                disabled
                                className="text-gray-300 cursor-not-allowed font-medium"
                              >
                                Update role
                              </button>
                            ) : (
                              <button
                                type="button"
                                onClick={() => handleUpdateRoleOpen(user)}
                                className="text-blue-600 hover:text-blue-800 font-medium"
                              >
                                Update role
                              </button>
                            )
                          )}
                          {canDeleteUser(role) && (
                            <>
                              {showUpdateRole && <span className="text-gray-300">|</span>}
                              {isCurrentUser(user) ? (
                                <button
                                  disabled
                                  className="text-gray-300 cursor-not-allowed font-medium"
                                >
                                  Delete
                                </button>
                              ) : (
                                <button
                                  onClick={() => handleDelete(user)}
                                  className="text-red-600 hover:text-red-800 font-medium"
                                >
                                  Delete
                                </button>
                              )}
                            </>
                          )}
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* Create User modal */}
      {createModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50">
          <div className="bg-white rounded-lg shadow-lg border border-gray-200 w-full max-w-md p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Create User (Registration)</h3>
            <form onSubmit={handleCreateUserSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                <input
                  type="text"
                  value={createUsername}
                  onChange={(e) => setCreateUsername(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input
                  type="email"
                  value={createEmail}
                  onChange={(e) => setCreateEmail(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                <input
                  type="password"
                  value={createPassword}
                  onChange={(e) => setCreatePassword(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                  minLength={8}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
                <select
                  value={createRole}
                  onChange={(e) => setCreateRole(e.target.value as UserRole)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {ROLES.map((r) => (
                    <option key={r} value={r}>{r}</option>
                  ))}
                </select>
              </div>
              {createError && <div className="text-red-600 text-sm">{createError}</div>}
              <div className="flex gap-2 justify-end pt-2">
                <button
                  type="button"
                  onClick={() => setCreateModalOpen(false)}
                  className="px-4 py-2 border border-gray-300 rounded text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={createSubmitting}
                  className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                >
                  {createSubmitting ? 'Creating...' : 'Create User'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Update Role modal */}
      {updateRoleUser && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50">
          <div className="bg-white rounded-lg shadow-lg border border-gray-200 w-full max-w-sm p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Update User Role</h3>
            <p className="text-sm text-gray-600 mb-4">
              {updateRoleUser.username ?? updateRoleUser.email ?? `User #${updateRoleUser.id}`}
            </p>
            <form onSubmit={handleUpdateRoleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
                <select
                  value={updateRoleValue}
                  onChange={(e) => setUpdateRoleValue(e.target.value as UserRole)}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {ROLES.map((r) => (
                    <option key={r} value={r}>{r}</option>
                  ))}
                </select>
              </div>
              {updateRoleError && <div className="text-red-600 text-sm">{updateRoleError}</div>}
              <div className="flex gap-2 justify-end pt-2">
                <button
                  type="button"
                  onClick={() => setUpdateRoleUser(null)}
                  className="px-4 py-2 border border-gray-300 rounded text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={updateRoleSubmitting}
                  className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                >
                  {updateRoleSubmitting ? 'Updating...' : 'Update Role'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

