import { UserRole } from '../types';

/**
 * Decodes a JWT token and extracts the role from the payload
 * This is a fallback mechanism in case the role isn't stored separately
 */
export const decodeJWT = (token: string): { role?: UserRole; sub?: string; exp?: number } | null => {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) {
      return null;
    }

    const payload = parts[1];
    const decoded = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
    
    return {
      role: decoded.role as UserRole,
      sub: decoded.sub,
      exp: decoded.exp,
    };
  } catch (error) {
    console.error('Failed to decode JWT:', error);
    return null;
  }
};

/**
 * Gets the role from JWT token if available
 */
export const getRoleFromToken = (token: string | null): UserRole | null => {
  if (!token) return null;
  const decoded = decodeJWT(token);
  return decoded?.role || null;
};
