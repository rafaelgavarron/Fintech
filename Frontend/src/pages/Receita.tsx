import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { incomeService } from '../services/incomeService';
import type { Income } from '../types/income';

function Receita() {
  const { currentOrganization } = useAuth();
  const [receitas, setReceitas] = useState<Income[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (currentOrganization) {
      loadIncomes();
    }
  }, [currentOrganization]);

  const loadIncomes = async () => {
    if (!currentOrganization) return;

    try {
      setLoading(true);
      const data = await incomeService.getByOrganization(currentOrganization.id);
      setReceitas(data);
    } catch (error) {
      console.error('Erro ao carregar receitas:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Confirmar remoção da receita?')) {
      try {
        await incomeService.delete(id);
        loadIncomes();
      } catch (error) {
        console.error('Erro ao deletar receita:', error);
        alert('Erro ao deletar receita');
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
        <h1 className="h3 fw-bold text-success mb-4">Listagem de Receitas</h1>

        {loading ? (
          <p>Carregando receitas...</p>
        ) : (
          <>
            <Link to="/receitas/novo" className="btn btn-success mb-3">
              + Registrar Nova Receita
            </Link>

            <table className="table table-striped table-dark">
              <thead>
                <tr>
                  <th className="text-light">Nome</th>
                  <th className="text-light">Valor</th>
                  <th className="text-light">Categoria</th>
                  <th className="text-light">Data</th>
                  <th className="text-light">Descrição</th>
                  <th className="text-light">Ações</th>
                </tr>
              </thead>
              <tbody>
                {receitas.map((r) => (
                  <tr key={r.id}>
                    <td>{r.name}</td>
                    <td>{formatCurrency(r.incomeAmount)}</td>
                    <td>
                      <span className="badge bg-secondary">
                        {r.category || 'Sem categoria'}
                      </span>
                    </td>
                    <td>{formatDate(r.incomeDate)}</td>
                    <td>{r.description || '-'}</td>
                    <td>
                      <Link
                        to={`/receitas/editar/${r.id}`}
                        className="btn btn-sm btn-info me-2"
                      >
                        Editar
                      </Link>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => handleDelete(r.id)}
                      >
                        Remover
                      </button>
                    </td>
                  </tr>
                ))}
                {receitas.length === 0 && (
                  <tr>
                    <td colSpan={6}>Nenhuma receita cadastrada.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </>
        )}
      </div>
    </div>
  );
}

export default Receita;