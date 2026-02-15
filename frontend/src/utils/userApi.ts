import { UserRole } from '../types';

const BASE_URL = 'http://localhost:8080';
const TOKEN_STORAGE_KEY = 'jwtToken';

const getAuthHeaders = () => {
  const token = typeof window !== 'undefined' ? localStorage.getItem(TOKEN_STORAGE_KEY) : null;

  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

// Get all users
export const userApi = {
  async getAll(): Promise<any[]> {
    const res = await fetch(`${BASE_URL}/api/users`, {
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to fetch users');
    }

    return res.json();
  },

  async remove(id: string): Promise<void> {
    const res = await fetch(`${BASE_URL}/api/users/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to delete user');
    }
  },

  async updateRole(userId: string, role: UserRole): Promise<void> {
    const url = `${BASE_URL}/api/users/${userId}/role?role=${encodeURIComponent(role)}`;
    const res = await fetch(url, {
      method: 'PUT',
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to update user role');
    }
  },
};






