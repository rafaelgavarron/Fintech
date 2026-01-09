// components/Header.tsx

import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

function Header({ toggleSidebar }: { toggleSidebar: () => void }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);
  
  const handleLogout = () => {
    logout();
    navigate('/login');
    setShowDropdown(false);
  };
  
  const handleNotificationClick = () => {
      console.log('Notificações abertas.');
  };

  return (
    <header className="app-header bg-secondary-dark px-4 d-flex justify-content-between align-items-center">
        
        {/* Lado Esquerdo: Botão Hamburguer (visível APENAS em Mobile) */}
        <div className="d-flex align-items-center">
            {/* Botão para abrir a sidebar em mobile */}
            <button 
                className="btn btn-sm btn-icon text-muted me-3 d-md-none" 
                onClick={toggleSidebar} 
                title="Abrir Menu"
            >
                <i className="bi bi-list fs-4"></i>
            </button>
        </div>

        {/* Lado Direito: Ações e Perfil do Usuário */}
        <div className="d-flex align-items-center">
            {/* Botão de Notificação */}
            <button 
                className="btn btn-icon me-3 bg-transparent border-0 position-relative"
                onClick={handleNotificationClick}
            >
                <i className="bi bi-bell-fill fs-5 text-muted"></i>
                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    3
                    <span className="visually-hidden">Novas notificações</span>
                </span>
            </button>
            
            {/* Ícone de Configurações (Engrenagem) */}
            <Link to="/settings/personal" className="btn btn-icon me-3 bg-transparent border-0 d-none d-sm-block">
                <i className="bi bi-gear-fill fs-5 text-muted"></i>
            </Link>

            {/* Dropdown de Usuário */}
            <div className="dropdown" style={{ position: 'relative' }}>
                <button
                    onClick={() => setShowDropdown(!showDropdown)}
                    className="d-flex align-items-center text-light text-decoration-none border-0 bg-transparent p-0"
                    type="button"
                    style={{ cursor: 'pointer' }}
                >
                    <i className="bi bi-person-circle fs-4 me-2"></i>
                    <strong className="d-none d-sm-inline">{user ? user.name.split(' ')[0] : 'Usuário'}</strong>
                </button>
                
                {showDropdown && (
                    <div className="dropdown-menu dropdown-menu-dark text-small shadow show" style={{ 
                        display: 'block', 
                        position: 'absolute', 
                        top: '100%', 
                        right: '0', 
                        marginTop: '10px',
                        minWidth: '220px',
                        zIndex: 1000 
                    }}>
                        <Link to="/settings/personal" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                            <i className="bi bi-person-fill me-2"></i>
                            Configurações Pessoais
                        </Link>
                        <Link to="/settings/organization" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                            <i className="bi bi-building me-2"></i>
                            Configurações da Organização
                        </Link>
                        <Link to="/organizations" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                            <i className="bi bi-list-ul me-2"></i>
                            Lista de Organizações
                        </Link>
                        <hr className="dropdown-divider" />
                        <button className="dropdown-item text-danger" onClick={handleLogout}> 
                            <i className="bi bi-box-arrow-right me-2"></i>
                            Sair
                        </button>
                    </div>
                )}
            </div>
        </div>
    </header>
  );
}

export default Header;