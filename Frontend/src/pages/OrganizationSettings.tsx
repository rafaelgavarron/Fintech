import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { organizationService } from '../services/organizationService';

function OrganizationSettings() {
  const { currentOrganization, refreshUserOrganizations, setCurrentOrganization } = useAuth();
  const [name, setName] = useState('');
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (currentOrganization) {
      setName(currentOrganization.name);
    }
  }, [currentOrganization]);

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setLoading(true);

    try {
      if (!currentOrganization) return;

      // Atualizar mantendo os valores atuais de isActive e trialExpireAt
      const updatedOrg = await organizationService.update(
        currentOrganization.id, 
        name, 
        currentOrganization.isActive, 
        currentOrganization.trialExpireAt
      );
      setMessage({ type: 'success', text: 'Organização atualizada com sucesso!' });
      
      // Atualizar as organizações e definir a atualizada como current
      await refreshUserOrganizations();
      setCurrentOrganization(updatedOrg);
    } catch (error: any) {
      setMessage({ type: 'error', text: error.message || 'Erro ao atualizar organização' });
    } finally {
      setLoading(false);
    }
  };

  if (!currentOrganization) {
    return (
      <div className="main-content">
        <div className="container-fluid">
          <div className="alert alert-warning">Nenhuma organização selecionada</div>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        <h1 className="h3 fw-bold text-light">Configurações da Organização</h1>
        <p className="text-muted mb-5">Gerencie as configurações da sua organização</p>

        {message && (
          <div className={`alert alert-${message.type === 'success' ? 'success' : 'danger'}`}>
            {message.text}
          </div>
        )}

        <div className="card shadow-sm p-4">
          <h5 className="mb-4 text-light">Informações da Organização</h5>
          <form onSubmit={handleUpdate}>
            <div className="mb-3">
              <label className="form-label text-light">Nome da Organização</label>
              <input
                type="text"
                className="form-control"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label text-light">Status</label>
              <input
                type="text"
                className="form-control"
                value={currentOrganization.isActive ? 'Ativa' : 'Inativa'}
                disabled
              />
            </div>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : 'Salvar Alterações'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default OrganizationSettings;