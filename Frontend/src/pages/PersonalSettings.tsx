// pages/PersonalSettings.tsx

import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { userService } from '../services/userService';
import { Link, useLocation } from 'react-router-dom';

// Componente para navegação de sub-configurações
const SettingsNav = () => {
    const location = useLocation();
    const isActive = (path: string) => location.pathname.startsWith(path);

    return (
        <div className="list-group list-group-flush shadow-sm" style={{ 
            borderRadius: '12px',
            overflow: 'hidden',
        }}>
            <Link 
                to="/settings/personal" 
                className={`list-group-item list-group-item-action ${isActive('/settings/personal') ? 'active' : ''}`}
                style={{ backgroundColor: 'var(--color-secondary-dark)', color: 'var(--color-text-light)', border: 'none' }}
            >
                <i className="bi bi-person-fill me-2"></i> Perfil e Conta
            </Link>
            <Link 
                to="/settings/organization" 
                className={`list-group-item list-group-item-action ${isActive('/settings/organization') ? 'active' : ''}`}
                style={{ backgroundColor: 'var(--color-secondary-dark)', color: 'var(--color-text-light)', border: 'none' }}
            >
                <i className="bi bi-building me-2"></i> Organização
            </Link>
            <Link 
                to="/bank-accounts" 
                className={`list-group-item list-group-item-action ${isActive('/bank-accounts') ? 'active' : ''}`}
                style={{ backgroundColor: 'var(--color-secondary-dark)', color: 'var(--color-text-light)', border: 'none' }}
            >
                <i className="bi bi-bank me-2"></i> Contas Bancárias
            </Link>
            <Link 
                to="/organizations" 
                className={`list-group-item list-group-item-action ${isActive('/organizations') ? 'active' : ''}`}
                style={{ backgroundColor: 'var(--color-secondary-dark)', color: 'var(--color-text-light)', border: 'none' }}
            >
                <i className="bi bi-list-ul me-2"></i> Lista de Organizações
            </Link>
        </div>
    );
};


function PersonalSettings() {
  const { user } = useAuth();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setName(user.name);
      setEmail(user.email);
    }
  }, [user]);

  const handleUpdateProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setLoading(true);

    try {
      if (!user) return;

      // Chama a função de atualização do serviço e atualiza o estado local
      await userService.updateUser(user.id, name);
      setMessage({ type: 'success', text: 'Perfil atualizado com sucesso!' });
    } catch (error: any) {
      setMessage({ type: 'error', text: error.message || 'Erro ao atualizar perfil' });
    } finally {
      setLoading(false);
    }
  };

  const handleUpdatePassword = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    if (password !== confirmPassword) {
      setMessage({ type: 'error', text: 'As senhas não coincidem' });
      return;
    }

    if (password.length < 6) {
      setMessage({ type: 'error', text: 'A senha deve ter no mínimo 6 caracteres' });
      return;
    }

    setLoading(true);

    try {
      if (!user) return;

      await userService.updateUser(user.id, undefined, password);
      setMessage({ type: 'success', text: 'Senha atualizada com sucesso!' });
      setPassword('');
      setConfirmPassword('');
    } catch (error: any) {
      setMessage({ type: 'error', text: error.message || 'Erro ao atualizar senha' });
    } finally {
      setLoading(false);
    }
  };

  if (!user) {
    return <div>Carregando...</div>;
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        <h1 className="h3 fw-bold text-light mb-1">Configurações</h1>
        <p className="text-muted mb-5">Gerencie suas configurações pessoais e de organização.</p>

        <div className="row g-4">
            {/* Coluna 1: Navegação */}
            <div className="col-md-3">
                <SettingsNav />
            </div>

            {/* Coluna 2: Conteúdo Principal */}
            <div className="col-md-9">
                <h3 className="h4 fw-bold text-light mb-4">Perfil e Conta</h3>
                
                {message && (
                    <div className={`alert alert-${message.type === 'success' ? 'success' : 'danger'} mb-4`}>
                        {message.text}
                    </div>
                )}
                
                {/* Seção de Avatar/Foto */}
                <div className="card shadow-sm p-4 mb-5">
                    <h5 className="mb-4 text-light border-bottom pb-2">Foto de Perfil</h5>
                    <div className="d-flex align-items-center">
                        <i className="bi bi-person-circle display-1 me-4 text-primary"></i>
                        <div>
                            <p className="mb-1 text-light fw-bold">{user.name}</p>
                            <button className="btn btn-sm btn-outline-secondary">
                                <i className="bi bi-upload me-1"></i> Alterar Foto
                            </button>
                        </div>
                    </div>
                </div>

                <div className="row">
                    {/* Seção de Informações do Perfil */}
                    <div className="col-lg-6">
                        <div className="card shadow-sm p-4 mb-4 h-100">
                            <h5 className="mb-4 text-light">Informações do Perfil</h5>
                            <form onSubmit={handleUpdateProfile}>
                                <div className="mb-3">
                                    <label className="form-label text-light">Nome</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label text-light">E-mail</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        value={email}
                                        disabled
                                    />
                                    <small className="text-muted">O e-mail não pode ser alterado</small>
                                </div>
                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? 'Salvando...' : 'Atualizar Perfil'}
                                </button>
                            </form>
                        </div>
                    </div>

                    {/* Seção de Alterar Senha */}
                    <div className="col-lg-6">
                        <div className="card shadow-sm p-4 h-100">
                            <h5 className="mb-4 text-light">Alterar Senha</h5>
                            <form onSubmit={handleUpdatePassword}>
                                <div className="mb-3">
                                    <label className="form-label text-light">Nova Senha</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label text-light">Confirmar Nova Senha</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <button type="submit" className="btn btn-warning" disabled={loading}>
                                    {loading ? 'Salvando...' : 'Alterar Senha'}
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      </div>
    </div>
  );
}

export default PersonalSettings;