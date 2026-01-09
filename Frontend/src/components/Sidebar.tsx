// components/Sidebar.tsx

import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const isActive = (location: any, path: string) => {
  if (path === '/') return location.pathname === path;
  if (path === '/transacoes')
    return (
      location.pathname.startsWith('/despesas') ||
      location.pathname.startsWith('/receitas')
    );
  if (path === '/metas') return location.pathname.startsWith('/metas');
  if (path === '/investimentos')
    return location.pathname.startsWith('/investimentos');
  if (path === '/settings')
    return (
      location.pathname.startsWith('/settings') ||
      location.pathname.startsWith('/organizations') ||
      location.pathname.startsWith('/bank-accounts')
    );

  return location.pathname.startsWith(path);
};

function Sidebar({
  isCollapsed,
  toggleSidebar,
}: {
  isCollapsed: boolean;
  toggleSidebar: () => void;
}) {
  const { user, currentOrganization, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // Ação para fechar a sidebar em mobile após clicar em um link
  const handleLinkClick = () => {
    if (window.innerWidth < 834) {
      // Em mobile, ao clicar em um link, feche a barra (toggleSidebar)
      toggleSidebar();
    }
  };

  return (
    <>
      {/* Overlay Mobile para fechar o menu ao clicar fora */}
      {!isCollapsed && (
        <div
          className="d-md-none"
          onClick={toggleSidebar}
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor: 'rgba(0,0,0,0.6)',
            zIndex: 1070,
          }}
        />
      )}
      <nav
        className={`sidebar d-flex flex-column p-0 vh-100 ${
          isCollapsed ? 'collapsed' : ''
        }`}
      >
        {/* Logo da Aplicação e Botão de Toggle */}
        <div className="sidebar-header d-flex align-items-center p-4">
          {/* Botão de Toggle/Retração visível em DESKTOP */}
          <button
            className="btn btn-sm btn-icon text-muted d-none d-md-block me-2"
            onClick={toggleSidebar}
            title={isCollapsed ? 'Expandir' : 'Recolher'}
          >
            {isCollapsed ? (
              <i className="bi bi-arrow-bar-right fs-5"></i>
            ) : (
              <i className="bi bi-arrow-bar-left fs-5"></i>
            )}
          </button>

          <Link
            to="/"
            className="d-flex align-items-center text-decoration-none me-auto"
            onClick={handleLinkClick}
          >
            <i className="bi bi-currency-dollar text-primary fs-4 me-2"></i>
            {!isCollapsed && <h4 className="m-0 text-brand-accent">FinPath</h4>}
          </Link>

          {/* Botão de Fechar (X) visível apenas em MOBILE (quando aberta) */}
          {window.innerWidth < 834 && !isCollapsed && (
            <button
              className="btn btn-sm btn-icon text-muted d-md-none"
              onClick={toggleSidebar} // Fecha o menu em mobile
              title="Fechar Menu"
              style={{ position: 'absolute', right: '10px' }} // Posicionamento Absoluto para o mobile
            >
              <i className="bi bi-x-lg fs-5"></i>
            </button>
          )}
        </div>

        {/* Navegação Principal (Links) */}
        <ul className="nav nav-pills flex-column mb-auto px-3 py-2">
          <li className="nav-item mb-1">
            <Link
              to="/"
              className={`nav-link py-2 ${
                isActive(location, '/') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-grid-1x2-fill me-3 fs-5"></i>
              <span className="nav-link-text">Dashboard</span>
            </Link>
          </li>
          <li className="nav-item mb-1">
            <Link
              to="/despesas"
              className={`nav-link py-2 ${
                isActive(location, '/transacoes') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-arrow-down-up me-3 fs-5"></i>
              <span className="nav-link-text">Transações</span>
            </Link>
          </li>
          <li className="nav-item mb-1">
            <Link
              to="/metas"
              className={`nav-link py-2 ${
                isActive(location, '/metas') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-rocket-takeoff-fill me-3 fs-5"></i>
              <span className="nav-link-text">Metas</span>
            </Link>
          </li>
          <li className="nav-item mb-1">
            <Link
              to="/investimentos"
              className={`nav-link py-2 ${
                isActive(location, '/investimentos') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-graph-up-arrow me-3 fs-5"></i>
              <span className="nav-link-text">Investimentos</span>
            </Link>
          </li>
          <li className="nav-item mb-1">
            <Link
              to="/settings/personal"
              className={`nav-link py-2 ${
                isActive(location, '/settings') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-gear-fill me-3 fs-5"></i>
              <span className="nav-link-text">Configurações</span>
            </Link>
          </li>
          <li className="nav-item mb-1">
            <Link
              to="/ajuda"
              className={`nav-link py-2 ${
                isActive(location, '/ajuda') ? 'active' : ''
              }`}
              onClick={handleLinkClick}
            >
              <i className="bi bi-question-circle-fill me-3 fs-5"></i>
              <span className="nav-link-text">Ajuda</span>
            </Link>
          </li>
        </ul>

        {/* Footer Fixo da Sidebar (Perfil do Usuário) */}
        <div className="sidebar-footer p-3 mt-auto">
          <div className="d-flex align-items-center text-light bg-dark-secondary rounded-3 p-2">
            <i className="bi bi-person-circle fs-4 me-3 text-primary"></i>
            {!isCollapsed && (
              <div className="d-flex flex-column me-auto">
                <span className="fw-bold small">
                  {user ? user.name.split(' ')[0] : 'Usuário'}
                </span>
                <span className="text-muted small">
                  {currentOrganization ? currentOrganization.name : 'Sem Org.'}
                </span>
              </div>
            )}
            <button
              onClick={handleLogout}
              className="btn btn-sm btn-icon text-danger ms-2 border-0 p-1"
              title="Sair"
            >
              <i className="bi bi-box-arrow-right"></i>
            </button>
          </div>
        </div>
      </nav>
    </>
  );
}

export default Sidebar;
