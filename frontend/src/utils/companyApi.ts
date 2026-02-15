import { Company } from '../types';

const BASE_URL = 'http://localhost:8080';
const TOKEN_STORAGE_KEY = 'jwtToken';

const getAuthHeaders = () => {
  const token = typeof window !== 'undefined' ? localStorage.getItem(TOKEN_STORAGE_KEY) : null;

  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

export interface CompanySearchParams {
  search?: string;
  page?: number;
  size?: number;
}

export interface CompanySearchResult {
  companies: Company[];
  totalPages: number;
  totalElements: number;
}

export const companyApi = {
  async getAll(): Promise<Company[]> {
    const res = await fetch(`${BASE_URL}/api/companies`, {
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to fetch companies');
    }

    return res.json();
  },

  async getById(id: string): Promise<Company> {
    const res = await fetch(`${BASE_URL}/api/companies/${id}`, {
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to fetch company');
    }

    return res.json();
  },

  async create(payload: Omit<Company, 'id'>): Promise<Company> {
    const res = await fetch(`${BASE_URL}/api/companies`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      throw new Error('Failed to create company');
    }

    return res.json();
  },

  async update(id: string, payload: Omit<Company, 'id'>): Promise<Company> {
    const res = await fetch(`${BASE_URL}/api/companies/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      throw new Error('Failed to update company');
    }

    return res.json();
  },

  async remove(id: string): Promise<void> {
    const res = await fetch(`${BASE_URL}/api/companies/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to delete company');
    }
  },

  // Uses POST /api/companies/search for server-side search + pagination.
  // Assumes a Spring Data–style response:
  // { content: Company[]; totalPages: number; totalElements: number; ... }
  async search(params: CompanySearchParams): Promise<CompanySearchResult> {
    const searchTerm = (params.search ?? '').trim();
    const page = params.page ?? 0;
    const size = params.size ?? 10;

    // Helper to call the backend with a specific filter.
    const performSearch = async (payload: {
      companyName?: string | null;
      registrationNumber?: string | null;
      city?: string | null;
      state?: string | null;
      primaryContactEmail?: string | null;
    }): Promise<CompanySearchResult> => {
      const res = await fetch(`${BASE_URL}/api/companies/search`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          companyName: null,
          registrationNumber: null,
          city: null,
          state: null,
          primaryContactEmail: null,
          page,
          size,
          ...payload,
        }),
      });

      if (!res.ok) {
        throw new Error('Failed to search companies');
      }

      const data = await res.json();

      const companies: Company[] =
        (data?.content as Company[] | undefined) ??
        (data?.items as Company[] | undefined) ??
        (data as Company[] | undefined) ??
        [];

      const totalPages: number = typeof data?.totalPages === 'number' ? data.totalPages : 1;
      const totalElements: number =
        typeof data?.totalElements === 'number' ? data.totalElements : companies.length;

      return { companies, totalPages, totalElements };
    };

    // Empty search ⇒ no filters, just pagination.
    if (!searchTerm) {
      return performSearch({});
    }

    // If it looks like an email, search by primaryContactEmail.
    if (searchTerm.includes('@')) {
      return performSearch({ primaryContactEmail: searchTerm });
    }

    // If it contains digits, treat it as a registration number.
    if (/\d/.test(searchTerm)) {
      return performSearch({ registrationNumber: searchTerm });
    }

    // Otherwise, try name, then city, then state.
    let result = await performSearch({ companyName: searchTerm });
    if (result.companies.length > 0) {
      return result;
    }

    result = await performSearch({ city: searchTerm });
    if (result.companies.length > 0) {
      return result;
    }

    return performSearch({ state: searchTerm });
  },
};

