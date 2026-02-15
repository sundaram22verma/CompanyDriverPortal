import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Header } from './components/Header';
import { Navigation } from './components/Navigation';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { CompaniesPage } from './pages/CompaniesPage';
import { DriversPage } from './pages/DriversPage';
import { UsersPage } from './pages/UsersPage';
import { TabType } from './types';

const AppContent: React.FC = () => {
  const { isAuthenticated, logout } = useAuth();
  const [activeTab, setActiveTab] = useState<TabType>('companies');
  const [authView, setAuthView] = useState<'login' | 'register'>('login');

  if (!isAuthenticated) {
    if (authView === 'register') {
      return <RegisterPage onSwitchToLogin={() => setAuthView('login')} />;
    }
    return <LoginPage onSwitchToRegister={() => setAuthView('register')} />;
  }

  const handleTabChange = (tab: TabType) => {
    setActiveTab(tab);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header onLogout={logout} />
      <Navigation activeTab={activeTab} onTabChange={handleTabChange} />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {activeTab === 'companies' && <CompaniesPage />}
        {activeTab === 'drivers' && <DriversPage />}
        {activeTab === 'users' && <UsersPage />}
      </main>
    </div>
  );
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default App;

