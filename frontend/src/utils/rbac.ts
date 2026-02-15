import { UserRole } from '../types';

/**
 * RBAC Permission Matrix
 * Based on the provided permissions table:
 * 
 * USER: Can only GET (view) and SEARCH. Cannot CREATE, UPDATE, or DELETE
 * ADMIN: Can GET, SEARCH, CREATE, UPDATE. Cannot DELETE
 * SUPER_ADMIN: Can do everything including DELETE
 */

export const canCreateCompany = (role: UserRole | null): boolean => {
  return role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canUpdateCompany = (role: UserRole | null): boolean => {
  return role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canDeleteCompany = (role: UserRole | null): boolean => {
  return role === 'SUPER_ADMIN';
};

export const canViewCompany = (role: UserRole | null): boolean => {
  return role === 'USER' || role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canSearchCompany = (role: UserRole | null): boolean => {
  return role === 'USER' || role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canCreateDriver = (role: UserRole | null): boolean => {
  return role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canUpdateDriver = (role: UserRole | null): boolean => {
  return role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canDeleteDriver = (role: UserRole | null): boolean => {
  return role === 'SUPER_ADMIN';
};

export const canViewDriver = (role: UserRole | null): boolean => {
  return role === 'USER' || role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canSearchDriver = (role: UserRole | null): boolean => {
  return role === 'USER' || role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canViewUsers = (role: UserRole | null): boolean => {
  return role === 'USER' || role === 'ADMIN' || role === 'SUPER_ADMIN';
};

export const canDeleteUser = (role: UserRole | null): boolean => {
  return role === 'SUPER_ADMIN';
};

export const canCreateUser = (role: UserRole | null): boolean => {
  return role === 'SUPER_ADMIN';
};

export const canUpdateUserRole = (role: UserRole | null): boolean => {
  return role === 'SUPER_ADMIN';
};
