import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { goalService } from '../services/goalService';
import type { Goal } from '../types/goal';

function Meta() {
  const { currentOrganization } = useAuth();
  const [metas, setMetas] = useState<Goal[]>([]);
  const [loading, setLoading] = useState(true);
  const [contributionsMap, setContributionsMap] = useState<Record<string, number>>({});

  useEffect(() => {
    if (currentOrganization) {
      loadGoals();
    }
  }, [currentOrganization]);

  useEffect(() => {
    if (metas.length > 0) {
      loadContributions();
    }
  }, [metas]);

  const loadGoals = async () => {
    if (!currentOrganization) return;

    try {
      setLoading(true);
      const data = await goalService.getByOrganization(currentOrganization.id);
      setMetas(data);
    } catch (error) {
      console.error('Erro ao carregar metas:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadContributions = async () => {
    const contributions: Record<string, number> = {};
    
    for (const goal of metas) {
      try {
        const total = await goalService.getContributionTotal(goal.id);
        contributions[goal.id] = total;
      } catch (error) {
        console.error(`Erro ao carregar contribuições da meta ${goal.id}:`, error);
        contributions[goal.id] = 0;
      }
    }
    
    setContributionsMap(contributions);
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Confirmar remoção da meta?')) {
      try {
        await goalService.delete(id);
        loadGoals();
      } catch (error) {
        console.error('Erro ao deletar meta:', error);
        alert('Erro ao deletar meta');
      }
    }
  };

  const formatDate = (timestamp: number) => {
    return new Date(timestamp * 1000).toLocaleDateString('pt-BR');
  };

  const formatCurrency = (cents: number) => {
    return (cents / 100).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
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
        <h1 className="h3 fw-bold text-primary">Metas Financeiras</h1>
        <p className="text-muted mb-4">Acompanhe seu progresso e estabeleça novos objetivos.</p>

        <div className="d-flex justify-content-end mb-4">
          <Link to="/metas/novo" className="btn btn-primary">
            <i className="bi bi-plus-circle-fill me-2"></i> Adicionar nova Meta
          </Link>
        </div>

        <div className="card shadow-sm" style={{ backgroundColor: 'var(--color-secondary-dark)', border: '1px solid #374151', borderRadius: '12px', padding: '1.5rem' }}>
          <h5 className="mb-4 text-light" style={{ fontFamily: 'Inter, sans-serif', fontWeight: 600, fontSize: '1.5rem' }}>Metas Ativas</h5>

          {loading ? (
            <div className="alert alert-warning" role="alert">
              Carregando dados...
            </div>
          ) : metas.length === 0 ? (
            <div className="alert alert-info" role="alert">
              Você ainda não possui nenhuma meta cadastrada. Use o botão "Adicionar nova Meta"
              para começar!
            </div>
          ) : (
            <div className="table-responsive">
              <table className="table" style={{ marginBottom: 0 }}>
                <thead>
                  <tr>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Meta</th>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Valor Alvo</th>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Valor Atual</th>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Progresso</th>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Data Limite</th>
                    <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {metas.map((m) => {
                    const currentValue = contributionsMap[m.id] || 0;
                    const progress = m.desiredAmount > 0 ? (currentValue / m.desiredAmount) * 100 : 0;
                    
                    return (
                      <tr key={m.id}>
                        <td className="fw-bold" style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>{m.name}</td>
                        <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>{formatCurrency(m.desiredAmount)}</td>
                        <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>{formatCurrency(currentValue)}</td>
                        <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', padding: '1rem 0.5rem' }}>
                          <div className="progress" style={{ height: '20px', backgroundColor: 'var(--color-primary-dark)' }}>
                            <div
                              className={`progress-bar ${progress >= 100 ? 'bg-success' : progress >= 50 ? 'bg-info' : 'bg-warning'}`}
                              role="progressbar"
                              style={{ width: `${Math.min(progress, 100)}%` }}
                            >
                              {progress.toFixed(1)}%
                            </div>
                          </div>
                        </td>
                        <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>{formatDate(m.dueDate)}</td>
                        <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>
                          <Link
                            to={`/metas/editar/${m.id}`}
                            className="btn btn-sm btn-info me-2"
                          >
                            Editar
                          </Link>
                          <button
                            className="btn btn-sm btn-danger"
                            onClick={() => handleDelete(m.id)}
                          >
                            Remover
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Meta;