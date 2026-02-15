import React, { useState, useEffect, useCallback } from 'react';
import { Plus } from 'lucide-react';
import { Company, ViewType } from '../types';
import { ITEMS_PER_PAGE } from '../utils/constants';
import { SearchBar } from '../components/SearchBar';
import { CompanyCard } from '../components/CompanyCard';
import { CompanyForm } from '../components/CompanyForm';
import { Pagination } from '../components/Pagination';
import { companyApi } from '../utils/companyApi';
import { useAuth } from '../context/AuthContext';
import { canCreateCompany, canUpdateCompany, canDeleteCompany } from '../utils/rbac';

export const CompaniesPage: React.FC = () => {
  const { role } = useAuth();
  const [companies, setCompanies] = useState<Company[]>([]);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);
  const [view, setView] = useState<ViewType>('list');
  const [editingCompany, setEditingCompany] = useState<Company | null>(null);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Load companies
  const loadCompanies = useCallback(
    async (searchValue: string, pageValue: number) => {
      try {
        setLoading(true);
        setError(null);

        // Assumes backend paging is 0-based; adjust if it's 1-based.
        const result = await companyApi.search({
          search: searchValue,
          page: pageValue - 1,
          size: ITEMS_PER_PAGE,
        });

        setCompanies(result.companies);
        setTotalPages(result.totalPages || 1);
      } catch (err) {
        console.error('Failed to load companies', err);
        setError('Failed to load companies. Please try again.');
      } finally {
        setLoading(false);
      }
    },
    []
  );

  useEffect(() => {
    loadCompanies(search, page);
  }, [search, page, loadCompanies]);

  // Handlers
  const handleSave = async (company: Company) => {
    try {
      if (company.id) {
        // Check update permission
        if (!canUpdateCompany(role)) {
          alert('You do not have permission to update companies.');
          return;
        }
        const { id, ...rest } = company;
        await companyApi.update(id, rest);
      } else {
        // Check create permission
        if (!canCreateCompany(role)) {
          alert('You do not have permission to create companies.');
          return;
        }
        const { id, ...rest } = company;
        await companyApi.create(rest);
      }

      setView('list');
      setEditingCompany(null);
      await loadCompanies(search, page);
    } catch (err) {
      console.error('Failed to save company', err);
      alert('Failed to save company. Please try again.');
    }
  };

  const handleEdit = (company: Company) => {
    // Check update permission
    if (!canUpdateCompany(role)) {
      alert('You do not have permission to edit companies.');
      return;
    }
    setEditingCompany(company);
    setView('entry');
  };

  const handleDelete = async (company: Company) => {
    if (!company.id) return;

    if (!canDeleteCompany(role)) {
      alert('You do not have permission to delete companies.');
      return;
    }

    if (!confirm(`Delete company "${company.companyName}"?`)) return;
    try {
      await companyApi.remove(company.id);
      await loadCompanies(search, page);
    } catch (err) {
      console.error('Failed to delete company', err);
      alert('Failed to delete company. Please try again.');
    }
  };

  const handleCancel = () => {
    setView('list');
    setEditingCompany(null);
  };

  const handleSearchChange = (value: string) => {
    setSearch(value);
    setPage(1);
  };

  if (view === 'entry') {
    return (
      <CompanyForm
        company={editingCompany}
        onSave={handleSave}
        onCancel={handleCancel}
      />
    );
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-semibold text-gray-900">Companies</h2>
        {canCreateCompany(role) && (
          <button
            onClick={() => setView('entry')}
            className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
          >
            <Plus size={18} />
            Add Company
          </button>
        )}
      </div>

      <SearchBar
        value={search}
        onChange={handleSearchChange}
        placeholder="Search by name, registration number, city, or state..."
      />

      {error && (
        <div className="mt-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded px-4 py-2">
          {error}
        </div>
      )}

      <div className="space-y-4">
        {loading ? (
          <div className="text-center py-12 bg-white rounded-lg border border-gray-200">
            <p className="text-gray-600">Loading companies...</p>
          </div>
        ) : companies.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-lg border border-gray-200">
            <p className="text-gray-600">No companies found</p>
          </div>
        ) : (
          companies.map((company) => (
            <CompanyCard
              key={company.id ?? company.registrationNumber}
              company={company}
              onEdit={handleEdit}
              onDelete={canDeleteCompany(role) ? handleDelete : undefined}
              canEdit={canUpdateCompany(role)}
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

