// src/App.tsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState, useEffect } from 'react';

import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Footer from './components/Footer';
import { ProtectedRoute } from './components/ProtectedRoute';

import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword.tsx';
import Investimentos from './pages/Investimentos';
import InvestimentoForm from './pages/InvestimentoForm';

import Despesa from './pages/Despesa';
import DespesaForm from './pages/DespesaForm';

import Receita from './pages/Receita';
import ReceitaForm from './pages/ReceitaForm';

import Meta from './pages/Meta';
import MetaForm from './pages/MetaForm';

import NotFound from './pages/NotFound';
import Ajuda from './pages/Ajuda';
import PersonalSettings from './pages/PersonalSettings';
import OrganizationSettings from './pages/OrganizationSettings';
import OrganizationsList from './pages/OrganizationsList';
import BankAccounts from './pages/BankAccounts';

function App() {
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarCollapsed((s) => !s);
  };

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 834) {
        setIsSidebarCollapsed(true);
      }
    };

    window.addEventListener('resize', handleResize);
    handleResize();

    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <Router>
      <Routes>
        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />

        {/* Layout com sidebar/header/footer e rotas protegidas */}
        <Route
          path="*"
          element={
            <div
              className={`app-layout-wrapper ${
                isSidebarCollapsed ? 'sidebar-collapsed' : ''
              }`}
            >
              <aside>
                <Sidebar
                  isCollapsed={isSidebarCollapsed}
                  toggleSidebar={toggleSidebar}
                />
              </aside>

              <div className="content-wrapper">
                <Header toggleSidebar={toggleSidebar} />

                <main className="main-content-area">
                  <Routes>
                    <Route
                      path="/"
                      element={
                        <ProtectedRoute>
                          <Home />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/investimentos"
                      element={
                        <ProtectedRoute>
                          <Investimentos />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/investimentos/novo"
                      element={
                        <ProtectedRoute>
                          <InvestimentoForm />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/investimentos/editar/:id"
                      element={
                        <ProtectedRoute>
                          <InvestimentoForm />
                        </ProtectedRoute>
                      }
                    />

                    <Route
                      path="/despesas"
                      element={
                        <ProtectedRoute>
                          <Despesa />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/despesas/novo"
                      element={
                        <ProtectedRoute>
                          <DespesaForm />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/despesas/editar/:id"
                      element={
                        <ProtectedRoute>
                          <DespesaForm />
                        </ProtectedRoute>
                      }
                    />

                    <Route
                      path="/receitas"
                      element={
                        <ProtectedRoute>
                          <Receita />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/receitas/novo"
                      element={
                        <ProtectedRoute>
                          <ReceitaForm />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/receitas/editar/:id"
                      element={
                        <ProtectedRoute>
                          <ReceitaForm />
                        </ProtectedRoute>
                      }
                    />

                    <Route
                      path="/metas"
                      element={
                        <ProtectedRoute>
                          <Meta />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/metas/novo"
                      element={
                        <ProtectedRoute>
                          <MetaForm />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/metas/editar/:id"
                      element={
                        <ProtectedRoute>
                          <MetaForm />
                        </ProtectedRoute>
                      }
                    />

                    <Route
                      path="/ajuda"
                      element={
                        <ProtectedRoute>
                          <Ajuda />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/settings/personal"
                      element={
                        <ProtectedRoute>
                          <PersonalSettings />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/settings/organization"
                      element={
                        <ProtectedRoute>
                          <OrganizationSettings />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/organizations"
                      element={
                        <ProtectedRoute>
                          <OrganizationsList />
                        </ProtectedRoute>
                      }
                    />
                    <Route
                      path="/bank-accounts"
                      element={
                        <ProtectedRoute>
                          <BankAccounts />
                        </ProtectedRoute>
                      }
                    />

                    <Route
                      path="*"
                      element={
                        <ProtectedRoute>
                          <NotFound />
                        </ProtectedRoute>
                      }
                    />
                  </Routes>
                </main>

                {/* Footer dentro do content-wrapper — ficará ao final do conteúdo */}
                <Footer />
              </div>
            </div>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
