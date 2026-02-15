import React from 'react';
import { Edit2, Trash2 } from 'lucide-react';
import { Driver } from '../types';

interface DriverCardProps {
  driver: Driver;
  onEdit: (driver: Driver) => void;
  onDelete?: (driver: Driver) => void;
  canEdit?: boolean;
}

export const DriverCard: React.FC<DriverCardProps> = ({ driver, onEdit, onDelete, canEdit = false }) => {
  return (
    <div className="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            {driver.firstName} {driver.lastName}
          </h3>
          <p className="text-sm text-gray-600">
            License: {driver.licenseNumber}
          </p>
        </div>
        {(canEdit || onDelete) && (
          <div className="flex gap-2">
            {canEdit && (
              <button
                onClick={() => onEdit(driver)}
                className="p-2 text-gray-600 hover:bg-gray-100 rounded transition-colors"
                aria-label="Edit driver"
              >
                <Edit2 size={18} />
              </button>
            )}
            {onDelete && (
              <button
                onClick={() => onDelete(driver)}
                className="p-2 text-red-600 hover:bg-red-50 rounded transition-colors"
                aria-label="Delete driver"
              >
                <Trash2 size={18} />
              </button>
            )}
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
        <div>
          <p className="text-gray-600">Date of Birth: {driver.dateOfBirth}</p>
          <p className="text-gray-600">Experience: {driver.experience} years</p>
          <p className="text-gray-600">Email: {driver.email}</p>
          <p className="text-gray-600">Mobile: {driver.mobile}</p>
        </div>
        <div>
          <p className="font-medium text-gray-700">Address</p>
          <p className="text-gray-600">
            {driver.addressLine1}
            {driver.addressLine2 && `, ${driver.addressLine2}`}
          </p>
          <p className="text-gray-600">
            {driver.city}, {driver.state} {driver.zipCode}
          </p>
        </div>
      </div>
    </div>
  );
};

