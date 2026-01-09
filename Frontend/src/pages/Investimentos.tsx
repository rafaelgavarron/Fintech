import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { investmentService } from '../services/investmentService';
import type { Investment } from '../types/investment';
import styles from '../styles/Investimentos.module.css';

function Investimentos() {
  const { currentOrganization } = useAuth();
  const [investments, setInvestments] = useState<Investment[]>([]);
  const [totalInvested, setTotalInvested] = useState(0);
  const [loading, setLoading] = useState(true);

  const loadInvestments = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      setLoading(true);
      const data = await investmentService.getByOrganization(
        currentOrganization.id
      );
      setInvestments(data);

      // Calcular total investido
      const total = data.reduce((sum, inv) => sum + inv.amount, 0);
      setTotalInvested(total);
    } catch (error) {
      console.error('Erro ao carregar investimentos:', error);
      setInvestments([]);
      setTotalInvested(0);
    } finally {
      setLoading(false);
    }
  }, [currentOrganization]);

  useEffect(() => {
    loadInvestments();
  }, [loadInvestments]);

  const handleDelete = async (id: string) => {
    if (window.confirm('Confirmar remoção do investimento?')) {
      try {
        await investmentService.delete(id);
        loadInvestments();
      } catch (error) {
        console.error('Erro ao deletar investimento:', error);
        alert('Erro ao deletar investimento');
      }
    }
  };

  const formatCurrency = (cents: number) => {
    return (cents / 100).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  };

  const formatDate = (timestamp: number) => {
    return new Date(timestamp * 1000).toLocaleDateString('pt-BR');
  };

  if (!currentOrganization) {
    return (
      <div className="main-content">
        <div className="container-fluid">
          <div className="alert alert-warning">
            Nenhuma organização selecionada
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        <div className={styles.header}>
          <h2 className={styles.h2}>Meus Investimentos</h2>
          <p className={styles.headerp}>
            Acompanhe a rentabilidade e o crescimento da sua carteira.
          </p>
        </div>
        <div className={styles.SummaryGrid}>
          <div className={styles.card}>
            <h6>Valor total investido</h6>
            <div className={`${styles.value} ${styles.textSuccess}`}>
              {loading ? 'Carregando...' : formatCurrency(totalInvested)}
            </div>
          </div>
          <div className={styles.card}>
            <h6>Total de ativos</h6>
            <div className={`${styles.value} ${styles.textSuccess}`}>
              {loading ? 'Carregando...' : investments.length}
            </div>
          </div>
        </div>
        <div className={styles.button}>
          <Link
            to="/investimentos/novo"
            className="btn btn-success bi bi-plus-lg"
          >
            {' '}
            Adicionar Investimento
          </Link>
        </div>
        <div className={styles.card}>
          <h5 className={styles.cardTitle}>Minha Carteira</h5>
          {loading ? (
            <div className="alert alert-info">Carregando investimentos...</div>
          ) : investments.length === 0 ? (
            <div className="alert alert-info">
              Nenhum investimento cadastrado ainda. Use o botão acima para
              adicionar um investimento.
            </div>
          ) : (
            <div className="table-responsive">
              <table className={`table ${styles.tableh}`}>
                <thead>
                  <tr>
                    <th>Ativo</th>
                    <th>Categoria</th>
                    <th>Valor (R$)</th>
                    <th>Data de Compra</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {investments.map((inv) => (
                    <tr key={inv.id}>
                      <td className="fw-bold">{inv.name}</td>
                      <td>{inv.category || 'Sem categoria'}</td>
                      <td className="fw-bold">{formatCurrency(inv.amount)}</td>
                      <td>{formatDate(inv.purchaseDate)}</td>
                      <td>
                        <Link
                          to={`/investimentos/editar/${inv.id}`}
                          className="btn btn-sm btn-info me-2"
                        >
                          Editar
                        </Link>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDelete(inv.id)}
                        >
                          Remover
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Investimentos;
