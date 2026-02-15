import { Company, Driver } from '../types';

// Extend Window interface for storage API
declare global {
  interface Window {
    storage?: {
      get: (key: string, shared?: boolean) => Promise<{ key: string; value: string; shared: boolean } | null>;
      set: (key: string, value: string, shared?: boolean) => Promise<{ key: string; value: string; shared: boolean } | null>;
      delete: (key: string, shared?: boolean) => Promise<{ key: string; deleted: boolean; shared: boolean } | null>;
      list: (prefix?: string, shared?: boolean) => Promise<{ keys: string[]; prefix?: string; shared: boolean } | null>;
    };
  }
}

const STORAGE_KEYS = {
  COMPANIES: 'companies',
  DRIVERS: 'drivers',
} as const;

// Fallback to localStorage if window.storage is not available
const useLocalStorageFallback = !window.storage;

export const storageUtils = {
  async getCompanies(): Promise<Company[]> {
    try {
      if (useLocalStorageFallback) {
        const data = localStorage.getItem(STORAGE_KEYS.COMPANIES);
        return data ? JSON.parse(data) : [];
      }
      
      const result = await window.storage!.get(STORAGE_KEYS.COMPANIES);
      return result ? JSON.parse(result.value) : [];
    } catch (error) {
      console.error('Error loading companies:', error);
      return [];
    }
  },

  async saveCompanies(companies: Company[]): Promise<void> {
    try {
      if (useLocalStorageFallback) {
        localStorage.setItem(STORAGE_KEYS.COMPANIES, JSON.stringify(companies));
        return;
      }
      
      await window.storage!.set(STORAGE_KEYS.COMPANIES, JSON.stringify(companies));
    } catch (error) {
      console.error('Error saving companies:', error);
      throw error;
    }
  },

  async getDrivers(): Promise<Driver[]> {
    try {
      if (useLocalStorageFallback) {
        const data = localStorage.getItem(STORAGE_KEYS.DRIVERS);
        return data ? JSON.parse(data) : [];
      }
      
      const result = await window.storage!.get(STORAGE_KEYS.DRIVERS);
      return result ? JSON.parse(result.value) : [];
    } catch (error) {
      console.error('Error loading drivers:', error);
      return [];
    }
  },

  async saveDrivers(drivers: Driver[]): Promise<void> {
    try {
      if (useLocalStorageFallback) {
        localStorage.setItem(STORAGE_KEYS.DRIVERS, JSON.stringify(drivers));
        return;
      }
      
      await window.storage!.set(STORAGE_KEYS.DRIVERS, JSON.stringify(drivers));
    } catch (error) {
      console.error('Error saving drivers:', error);
      throw error;
    }
  },
};

