import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import type { Organization } from '../types/organization';

function OrganizationsList() {
  const { userOrganizations, currentOrganization, setCurrentOrganization } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(false);
  }, [userOrganizations]);

  const handleSelectOrganization = (org: Organization) => {
    setCurrentOrganization(org);
  };

  if (loading) {
    return (
      <div className="main-content">
        <div className="container-fluid">
          <p>Carregando organizações...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        <header className="mb-5 pt-3">
          <h1 className="h3 fw-bold">Lista de Organizações</h1>
          <p className="text-muted">Selecione uma organização para trabalhar</p>
        </header>

        {userOrganizations.length === 0 ? (
          <div className="alert alert-info">
            Você não faz parte de nenhuma organização ainda.
          </div>
        ) : (
          <div className="row g-4">
            {userOrganizations.map((org) => (
              <div key={org.id} className="col-md-6 col-lg-4">
                <div
                  className={`card shadow-sm h-100 ${
                    currentOrganization?.id === org.id ? 'border-primary border-2' : ''
                  }`}
                  style={{ cursor: 'pointer' }}
                  onClick={() => handleSelectOrganization(org)}
                >
                  <div className="card-body">
                    <h5 className="card-title text-light">{org.name}</h5>
                    <p className="card-text">
                      <span className={`badge ${org.isActive ? 'bg-success' : 'bg-secondary'}`}>
                        {org.isActive ? 'Ativa' : 'Inativa'}
                      </span>
                    </p>
                    {currentOrganization?.id === org.id && (
                      <small className="text-primary">
                        <i className="bi bi-check-circle me-1"></i>
                        Organização Atual
                      </small>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default OrganizationsList;

