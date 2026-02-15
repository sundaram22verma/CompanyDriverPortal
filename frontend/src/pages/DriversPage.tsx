import React, { useState, useEffect, useCallback } from 'react';
import { Plus } from 'lucide-react';
import { Driver, ViewType } from '../types';
import { ITEMS_PER_PAGE } from '../utils/constants';
import { SearchBar } from '../components/SearchBar';
import { DriverCard } from '../components/DriverCard';
import { DriverForm } from '../components/DriverForm';
import { Pagination } from '../components/Pagination';
import { driverApi } from '../utils/driverApi';
import { useAuth } from '../context/AuthContext';
import { canCreateDriver, canUpdateDriver, canDeleteDriver } from '../utils/rbac';

export const DriversPage: React.FC = () => {
  const { role } = useAuth();
  const [drivers, setDrivers] = useState<Driver[]>([]);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);
  const [view, setView] = useState<ViewType>('list');
  const [editingDriver, setEditingDriver] = useState<Driver | null>(null);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadDrivers = useCallback(
    async (searchValue: string, pageValue: number) => {
      try {
        setLoading(true);
        setError(null);

        const result = await driverApi.search({
          search: searchValue,
          page: pageValue - 1,
          size: ITEMS_PER_PAGE,
        });

        setDrivers(result.drivers);
        setTotalPages(result.totalPages || 1);
      } catch (err) {
        console.error('Failed to load drivers', err);
        setError('Failed to load drivers. Please try again.');
      } finally {
        setLoading(false);
      }
    },
    []
  );

  useEffect(() => {
    loadDrivers(search, page);
  }, [search, page, loadDrivers]);

  const handleSave = async (driver: Driver) => {
    try {
      if (driver.id) {
        // Check update permission
        if (!canUpdateDriver(role)) {
          alert('You do not have permission to update drivers.');
          return;
        }
        const { id, ...rest } = driver;
        await driverApi.update(id, rest);
      } else {
        // Check create permission
        if (!canCreateDriver(role)) {
          alert('You do not have permission to create drivers.');
          return;
        }
        const { id, ...rest } = driver;
        await driverApi.create(rest);
      }

      setView('list');
      setEditingDriver(null);
      await loadDrivers(search, page);
    } catch (err) {
      console.error('Failed to save driver', err);
      alert('Failed to save driver. Please try again.');
    }
  };

  const handleEdit = (driver: Driver) => {
    // Check update permission
    if (!canUpdateDriver(role)) {
      alert('You do not have permission to edit drivers.');
      return;
    }
    setEditingDriver(driver);
    setView('entry');
  };

  const handleDelete = async (driver: Driver) => {
    if (!driver.id) return;
    
    // Check delete permission
    if (!canDeleteDriver(role)) {
      alert('You do not have permission to delete drivers.');
      return;
    }
    
    if (!confirm(`Delete ${driver.firstName} ${driver.lastName}?`)) return;
    try {
      await driverApi.remove(driver.id);
      await loadDrivers(search, page);
    } catch (err) {
      console.error('Failed to delete driver', err);
      alert('Failed to delete driver. Please try again.');
    }
  };

  const handleCancel = () => {
    setView('list');
    setEditingDriver(null);
  };

  const handleSearchChange = (value: string) => {
    setSearch(value);
    setPage(1);
  };

  if (view === 'entry') {
    return (
      <DriverForm
        driver={editingDriver}
        onSave={handleSave}
        onCancel={handleCancel}
      />
    );
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-semibold text-gray-900">Drivers</h2>
        {canCreateDriver(role) && (
          <button
            onClick={() => setView('entry')}
            className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
          >
            <Plus size={18} />
            Add Driver
          </button>
        )}
      </div>

      <SearchBar
        value={search}
        onChange={handleSearchChange}
        placeholder="Search by name, license number, email, or city..."
      />

      {error && (
        <div className="mt-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded px-4 py-2">
          {error}
        </div>
      )}

      <div className="space-y-4">
        {loading ? (
          <div className="text-center py-12 bg-white rounded-lg border border-gray-200">
            <p className="text-gray-600">Loading drivers...</p>
          </div>
        ) : drivers.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-lg border border-gray-200">
            <p className="text-gray-600">No drivers found</p>
          </div>
        ) : (
          drivers.map((driver) => (
            <DriverCard
              key={driver.id ?? driver.licenseNumber}
              driver={driver}
              onEdit={handleEdit}
              onDelete={canDeleteDriver(role) ? handleDelete : undefined}
              canEdit={canUpdateDriver(role)}
            />
          ))
        )}
      </div>

      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />
    </div>
  );
};

