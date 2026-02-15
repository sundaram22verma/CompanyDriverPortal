import { Driver } from '../types';

const BASE_URL = 'http://localhost:8080';
const TOKEN_STORAGE_KEY = 'jwtToken';

const getAuthHeaders = () => {
  const token = typeof window !== 'undefined' ? localStorage.getItem(TOKEN_STORAGE_KEY) : null;

  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

export interface DriverSearchParams {
  search?: string;
  page?: number;
  size?: number;
}

export interface DriverSearchResult {
  drivers: Driver[];
  totalPages: number;
  totalElements: number;
}

export const driverApi = {
  async getAll(): Promise<Driver[]> {
    const res = await fetch(`${BASE_URL}/api/drivers`, {
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to fetch drivers');
    }

    return res.json();
  },

  async getById(id: string): Promise<Driver> {
    const res = await fetch(`${BASE_URL}/api/drivers/${id}`, {
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to fetch driver');
    }

    return res.json();
  },

  async create(payload: Omit<Driver, 'id'>): Promise<Driver> {
    const res = await fetch(`${BASE_URL}/api/drivers`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      throw new Error('Failed to create driver');
    }

    return res.json();
  },

  async update(id: string, payload: Omit<Driver, 'id'>): Promise<Driver> {
    const res = await fetch(`${BASE_URL}/api/drivers/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      throw new Error('Failed to update driver');
    }

    return res.json();
  },

  async remove(id: string): Promise<void> {
    const res = await fetch(`${BASE_URL}/api/drivers/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });

    if (!res.ok) {
      throw new Error('Failed to delete driver');
    }
  },

  async search(params: DriverSearchParams): Promise<DriverSearchResult> {
    const searchTerm = (params.search ?? '').trim();
    const page = params.page ?? 0;
    const size = params.size ?? 10;

    // Helper to actually call the backend with a specific filter.
    const performSearch = async (payload: {
      firstName?: string | null;
      lastName?: string | null;
      email?: string | null;
      licenseNumber?: string | null;
      city?: string | null;
      state?: string | null;
    }): Promise<DriverSearchResult> => {
      const res = await fetch(`${BASE_URL}/api/drivers/search`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          firstName: null,
          lastName: null,
          email: null,
          licenseNumber: null,
          city: null,
          state: null,
          page,
          size,
          ...payload,
        }),
      });

      if (!res.ok) {
        throw new Error('Failed to search drivers');
      }

      const data = await res.json();

      const drivers: Driver[] =
        (data?.content as Driver[] | undefined) ??
        (data?.items as Driver[] | undefined) ??
        (data as Driver[] | undefined) ??
        [];

      const totalPages: number = typeof data?.totalPages === 'number' ? data.totalPages : 1;
      const totalElements: number =
        typeof data?.totalElements === 'number' ? data.totalElements : drivers.length;

      return { drivers, totalPages, totalElements };
    };

    // Empty search = no filters, just pagination.
    if (!searchTerm) {
      return performSearch({});
    }

    // Heuristics based on the placeholder:
    // "Search by name, license number, email, or city..."
    if (searchTerm.includes('@')) {
      // Looks like an email
      return performSearch({ email: searchTerm });
    }

    if (/\d/.test(searchTerm)) {
      // Contains digits – likely a license number
      return performSearch({ licenseNumber: searchTerm });
    }

    if (searchTerm.includes(' ')) {
      // Two-part name like "Samuel John" → first + last
      const [firstName, lastName] = searchTerm.split(/\s+/, 2);
      return performSearch({
        firstName: firstName || null,
        lastName: lastName || null,
      });
    }

    // Single word: try firstName, then lastName, then city.
    let result = await performSearch({ firstName: searchTerm });
    if (result.drivers.length > 0) {
      return result;
    }

    result = await performSearch({ lastName: searchTerm });
    if (result.drivers.length > 0) {
      return result;
    }

    return performSearch({ city: searchTerm });
  },
};
