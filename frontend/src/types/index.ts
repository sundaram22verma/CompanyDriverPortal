export interface Company {
  id?: string;
  companyName: string;
  establishedOn: string;
  registrationNumber: string;
  website: string;
  addressLine1: string;
  addressLine2: string;
  city: string;
  state: string;
  zipCode: string;
  primaryContactFirstName: string;
  primaryContactLastName: string;
  primaryContactEmail: string;
  primaryContactMobile: string;
}

export interface Driver {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  mobile: string;
  dateOfBirth: string;
  licenseNumber: string;
  experience: string;
  addressLine1: string;
  addressLine2: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface User {
  username: string;
  password: string;
}

export type TabType = 'companies' | 'drivers' | 'users';
export type ViewType = 'list' | 'entry';
export type UserRole = 'USER' | 'ADMIN' | 'SUPER_ADMIN';

