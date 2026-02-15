import React from 'react';
import { Edit2, Trash2 } from 'lucide-react';
import { Company } from '../types';

interface CompanyCardProps {
  company: Company;
  onEdit: (company: Company) => void;
  onDelete?: (company: Company) => void | Promise<void>;
  canEdit?: boolean;
}

export const CompanyCard: React.FC<CompanyCardProps> = ({ company, onEdit, onDelete, canEdit = false }) => {
  return (
    <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            {company.companyName}
          </h3>
          <p className="text-sm text-gray-600">
            Registration: {company.registrationNumber}
          </p>
        </div>
        {(canEdit || onDelete) && (
          <div className="flex gap-2">
            {canEdit && (
              <button
                onClick={() => onEdit(company)}
                className="p-2 text-gray-600 hover:bg-gray-100 rounded transition-colors"
                aria-label="Edit company"
              >
                <Edit2 size={18} />
              </button>
            )}
            {onDelete && (
              <button
                onClick={() => onDelete(company)}
                className="p-2 text-red-600 hover:bg-red-50 rounded transition-colors"
                aria-label="Delete company"
              >
                <Trash2 size={18} />
              </button>
            )}
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
        <div>
          <p className="text-gray-600">Established: {company.establishedOn}</p>
          <p className="text-gray-600">Website: {company.website || 'N/A'}</p>
          <p className="text-gray-600 mt-2">
            {company.addressLine1}
            {company.addressLine2 && `, ${company.addressLine2}`}
          </p>
          <p className="text-gray-600">
            {company.city}, {company.state} {company.zipCode}
          </p>
        </div>
        <div>
          <p className="font-medium text-gray-700">Primary Contact</p>
          <p className="text-gray-600">
            {company.primaryContactFirstName} {company.primaryContactLastName}
          </p>
          <p className="text-gray-600">{company.primaryContactEmail}</p>
          <p className="text-gray-600">{company.primaryContactMobile}</p>
        </div>
      </div>
    </div>
  );
};

