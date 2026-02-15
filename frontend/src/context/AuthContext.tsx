import React, { createContext, useContext, useState, ReactNode } from 'react';
import { UserRole } from '../types';
import { getRoleFromToken } from '../utils/jwt';

const LOGIN_API_URL = 'http://localhost:8080/api/auth/login';
const TOKEN_STORAGE_KEY = 'jwtToken';
const ROLE_STORAGE_KEY = 'userRole';

interface AuthContextType {
  isAuthenticated: boolean;
  role: UserRole | null;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
    // Initialize auth state from existing token if present
    if (typeof window === 'undefined') return false;
    return !!localStorage.getItem(TOKEN_STORAGE_KEY);
  });

  const [role, setRole] = useState<UserRole | null>(() => {
    // Initialize role from storage if present, or decode from token as fallback
    if (typeof window === 'undefined') return null;
    const storedRole = localStorage.getItem(ROLE_STORAGE_KEY);
    if (storedRole) {
      return storedRole as UserRole;
    }
    // Fallback: try to decode role from JWT token
    const token = localStorage.getItem(TOKEN_STORAGE_KEY);
    if (token) {
      const roleFromToken = getRoleFromToken(token);
      if (roleFromToken) {
        localStorage.setItem(ROLE_STORAGE_KEY, roleFromToken);
        return roleFromToken;
      }
    }
    return null;
  });

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      const res = await fetch(LOGIN_API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        return false;
      }

      // Assumes the API returns a JSON body that includes either `token` or `accessToken`
      // Adjust the property name if your backend uses something different.
      let token: string | undefined;
      let userRole: UserRole | undefined;
      try {
        const data = await res.json();
        token = data?.token ?? data?.accessToken;
        userRole = data?.role as UserRole;
      } catch {
        token = undefined;
        userRole = undefined;
      }

      if (token) {
        localStorage.setItem(TOKEN_STORAGE_KEY, token);
      }

      if (userRole) {
        localStorage.setItem(ROLE_STORAGE_KEY, userRole);
        setRole(userRole);
      }

      setIsAuthenticated(true);
      return true;
    } catch {
      return false;
    }
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
    localStorage.removeItem(ROLE_STORAGE_KEY);
    setIsAuthenticated(false);
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

