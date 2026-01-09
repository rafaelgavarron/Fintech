// pages/Home.tsx

import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { expenseService } from '../services/expenseService';
import { incomeService } from '../services/incomeService';
import { goalService } from '../services/goalService';
import type { Goal } from '../types/goal';

function Home() {
  const { user, currentOrganization } = useAuth();
  const [saldoAtual, setSaldoAtual] = useState(0);
  const [totalReceitasMes, setTotalReceitasMes] = useState(0);
  const [totalDespesasMes, setTotalDespesasMes] = useState(0);
  const [dataAtualizacao, setDataAtualizacao] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [metas, setMetas] = useState<Goal[]>([]);
  const [contributionsMap, setContributionsMap] = useState<
    Record<string, number>
  >({});
  const [expensesByCategory, setExpensesByCategory] = useState<
    Record<string, number>
  >({});
  const [incomesByCategory, setIncomesByCategory] = useState<
    Record<string, number>
  >({});
  const [timeSeriesData, setTimeSeriesData] = useState<
    Array<{
      date: string;
      expenses: number;
      incomes: number;
      balance: number;
    }>
  >([]);
  const [filterByCategory, setFilterByCategory] = useState<string>('');
  const [filterByBankAccount] = useState<string>('');
  const [filterByDateRange, setFilterByDateRange] = useState<{
    start: string;
    end: string;
  }>({
    start: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)
      .toISOString()
      .split('T')[0],
    end: new Date().toISOString().split('T')[0],
  });

  // Função para buscar o saldo total e os totais do mês
  const loadFinancialData = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      setLoading(true);
      setError(null);

      // Buscar dados reais do backend
      const [totalReceitasGeral, totalDespesasGeral] = await Promise.all([
        incomeService.getTotalByOrganization(currentOrganization.id),
        expenseService.getTotalByOrganization(currentOrganization.id),
      ]);

      // Para o mês atual, vamos usar os mesmos valores (backend pode não ter filtro por mês ainda)
      // Em produção, você pode criar um endpoint específico para filtrar por mês
      const receitasMes = totalReceitasGeral;
      const despesasMes = totalDespesasGeral;

      // Processamento dos dados (em Reais)
      const saldo = (totalReceitasGeral - totalDespesasGeral) / 100;
      const receitasDoMes = receitasMes / 100;
      const despesasDoMes = despesasMes / 100;

      setSaldoAtual(saldo);
      setTotalReceitasMes(receitasDoMes);
      setTotalDespesasMes(despesasDoMes);
      setDataAtualizacao(new Date().toLocaleDateString('pt-BR'));
    } catch (err: any) {
      console.error('Erro ao processar dados:', err);
      setError('Erro ao carregar dados financeiros.');
      // Em caso de erro, manter valores em 0
      setSaldoAtual(0);
      setTotalReceitasMes(0);
      setTotalDespesasMes(0);
    } finally {
      setLoading(false);
    }
  }, [currentOrganization]);

  // Função para carregar metas
  const loadGoals = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      const goals = await goalService.getByOrganization(currentOrganization.id);
      setMetas(goals);

      // Carregar contribuições para cada meta
      const contributions: Record<string, number> = {};
      for (const goal of goals) {
        try {
          const total = await goalService.getContributionTotal(goal.id);
          contributions[goal.id] = total;
        } catch (error) {
          console.error(
            `Erro ao carregar contribuições da meta ${goal.id}:`,
            error
          );
          contributions[goal.id] = 0;
        }
      }
      setContributionsMap(contributions);
    } catch (error) {
      console.error('Erro ao carregar metas:', error);
    }
  }, [currentOrganization]);

  // Função para carregar despesas por categoria
  const loadExpensesByCategory = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      const expenses = await expenseService.getByOrganization(
        currentOrganization.id
      );

      // Agrupar por categoria (apenas categorias definidas)
      const categoryTotals: Record<string, number> = {};
      expenses.forEach((expense) => {
        // Só conta se tiver categoria definida e não vazia
        if (expense.category && expense.category.trim() !== '') {
          const cat = expense.category.trim();
          categoryTotals[cat] =
            (categoryTotals[cat] || 0) + expense.expenseAmount;
        }
      });

      setExpensesByCategory(categoryTotals);
    } catch (error) {
      console.error('Erro ao carregar despesas por categoria:', error);
    }
  }, [currentOrganization]);

  // Função para carregar receitas por categoria
  const loadIncomesByCategory = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      const incomes = await incomeService.getByOrganization(
        currentOrganization.id
      );

      // Agrupar por categoria (apenas categorias definidas)
      const categoryTotals: Record<string, number> = {};
      incomes.forEach((income) => {
        // Só conta se tiver categoria definida e não vazia
        if (income.category && income.category.trim() !== '') {
          const cat = income.category.trim();
          categoryTotals[cat] =
            (categoryTotals[cat] || 0) + income.incomeAmount;
        }
      });

      setIncomesByCategory(categoryTotals);
    } catch (error) {
      console.error('Erro ao carregar receitas por categoria:', error);
    }
  }, [currentOrganization]);

  // Função para carregar dados de timeseries
  const loadTimeSeriesData = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      const [expenses, incomes] = await Promise.all([
        expenseService.getByOrganization(currentOrganization.id),
        incomeService.getByOrganization(currentOrganization.id),
      ]);

      // Filtrar por categoria se especificado
      let filteredExpenses = expenses;
      let filteredIncomes = incomes;

      if (filterByCategory) {
        filteredExpenses = expenses.filter(
          (e) => e.category === filterByCategory
        );
        filteredIncomes = incomes.filter(
          (i) => i.category === filterByCategory
        );
      }

      // Filtrar por conta bancária se especificado (quando implementado)
      // if (filterByBankAccount) {
      //     filteredExpenses = filteredExpenses.filter(e => e.bankAccountId === filterByBankAccount);
      //     filteredIncomes = filteredIncomes.filter(i => i.bankAccountId === filterByBankAccount);
      // }

      // Agrupar por data
      const dataByDate: Record<string, { expenses: number; incomes: number }> =
        {};

      filteredExpenses.forEach((expense) => {
        const date = new Date(expense.expenseDate * 1000)
          .toISOString()
          .split('T')[0];
        if (!dataByDate[date]) {
          dataByDate[date] = { expenses: 0, incomes: 0 };
        }
        dataByDate[date].expenses += expense.expenseAmount;
      });

      filteredIncomes.forEach((income) => {
        const date = new Date(income.incomeDate * 1000)
          .toISOString()
          .split('T')[0];
        if (!dataByDate[date]) {
          dataByDate[date] = { expenses: 0, incomes: 0 };
        }
        dataByDate[date].incomes += income.incomeAmount;
      });

      // Converter para array e ordenar por data
      let accumulatedBalance = 0;
      const timeSeries = Object.entries(dataByDate)
        .sort((a, b) => a[0].localeCompare(b[0]))
        .map(([date, data]) => {
          accumulatedBalance += (data.incomes - data.expenses) / 100;
          return {
            date,
            expenses: data.expenses / 100,
            incomes: data.incomes / 100,
            balance: accumulatedBalance,
          };
        });

      setTimeSeriesData(timeSeries);
    } catch (error) {
      console.error('Erro ao carregar dados de timeseries:', error);
    }
  }, [currentOrganization, filterByCategory, filterByBankAccount]);

  useEffect(() => {
    if (currentOrganization) {
      loadFinancialData();
      loadGoals();
      loadExpensesByCategory();
      loadIncomesByCategory();
    }
  }, [
    currentOrganization,
    loadFinancialData,
    loadGoals,
    loadExpensesByCategory,
    loadIncomesByCategory,
  ]);

  // Recarregar timeseries quando filtros mudarem
  useEffect(() => {
    if (currentOrganization) {
      loadTimeSeriesData();
    }
  }, [
    currentOrganization,
    filterByCategory,
    filterByDateRange,
    filterByBankAccount,
    loadTimeSeriesData,
  ]);

  const formatCurrency = (amount: number) => {
    return amount.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  };

  if (!user || !currentOrganization) {
    return (
      <div className="main-content">
        <div className="container-fluid">
          <div className="alert alert-info">
            Carregando dados da organização...
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        <div className="mb-4">
          <h2 className="h2 fw-bold text-light mb-1">
            Olá, {user.name.split(' ')[0]}!
          </h2>
          <p className="text-muted mb-0">
            Bem-vindo de volta ao seu painel financeiro.
          </p>
        </div>

        {error && <div className="alert alert-danger mb-4">{error}</div>}

        {/* Cards de Resumo Financeiro - Layout melhorado */}
        <div className="row g-4 mb-5">
          {/* Saldo Atual */}
          <div className="col-lg-3 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderLeft: '4px solid #007bff', borderRadius: '8px' }}
            >
              <div className="card-body p-4">
                <div className="d-flex align-items-center justify-content-between mb-3">
                  <div
                    className="p-2 rounded"
                    style={{ backgroundColor: 'rgba(0, 123, 255, 0.1)' }}
                  >
                    <i className="bi bi-wallet-fill text-primary fs-4"></i>
                  </div>
                </div>
                <h6 className="text-muted mb-2 small text-uppercase fw-semibold">
                  Saldo Atual
                </h6>
                {loading ? (
                  <p className="text-muted mb-0">Carregando...</p>
                ) : (
                  <>
                    <h3
                      className={`fw-bold mb-2 ${
                        saldoAtual >= 0 ? 'text-success' : 'text-danger'
                      }`}
                      style={{ fontSize: '1.8rem' }}
                    >
                      {formatCurrency(saldoAtual)}
                    </h3>
                    <small className="text-muted">
                      Atualizado em {dataAtualizacao || 'N/A'}
                    </small>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Receitas (Mês) */}
          <div className="col-lg-3 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderLeft: '4px solid #28a745', borderRadius: '8px' }}
            >
              <div className="card-body p-4">
                <div className="d-flex align-items-center justify-content-between mb-3">
                  <div
                    className="p-2 rounded"
                    style={{ backgroundColor: 'rgba(40, 167, 69, 0.1)' }}
                  >
                    <i className="bi bi-graph-up-arrow text-success fs-4"></i>
                  </div>
                </div>
                <h6 className="text-muted mb-2 small text-uppercase fw-semibold">
                  Receitas (Mês)
                </h6>
                {loading ? (
                  <p className="text-muted mb-0">Carregando...</p>
                ) : (
                  <h3
                    className="fw-bold text-success mb-0"
                    style={{ fontSize: '1.8rem' }}
                  >
                    {formatCurrency(totalReceitasMes)}
                  </h3>
                )}
              </div>
            </div>
          </div>

          {/* Despesas (Mês) */}
          <div className="col-lg-3 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderLeft: '4px solid #dc3545', borderRadius: '8px' }}
            >
              <div className="card-body p-4">
                <div className="d-flex align-items-center justify-content-between mb-3">
                  <div
                    className="p-2 rounded"
                    style={{ backgroundColor: 'rgba(220, 53, 69, 0.1)' }}
                  >
                    <i className="bi bi-graph-down-arrow text-danger fs-4"></i>
                  </div>
                </div>
                <h6 className="text-muted mb-2 small text-uppercase fw-semibold">
                  Despesas (Mês)
                </h6>
                {loading ? (
                  <p className="text-muted mb-0">Carregando...</p>
                ) : (
                  <h3
                    className="fw-bold text-danger mb-0"
                    style={{ fontSize: '1.8rem' }}
                  >
                    {formatCurrency(totalDespesasMes)}
                  </h3>
                )}
              </div>
            </div>
          </div>

          {/* Saldo Líquido */}
          <div className="col-lg-3 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderLeft: '4px solid #6f42c1', borderRadius: '8px' }}
            >
              <div className="card-body p-4">
                <div className="d-flex align-items-center justify-content-between mb-3">
                  <div
                    className="p-2 rounded"
                    style={{ backgroundColor: 'rgba(111, 66, 193, 0.1)' }}
                  >
                    <i className="bi bi-piggy-bank-fill text-info fs-4"></i>
                  </div>
                </div>
                <h6 className="text-muted mb-2 small text-uppercase fw-semibold">
                  Saldo Líquido
                </h6>
                {loading ? (
                  <p className="text-muted mb-0">Carregando...</p>
                ) : (
                  <h3
                    className={`fw-bold mb-0 ${
                      totalReceitasMes - totalDespesasMes >= 0
                        ? 'text-success'
                        : 'text-danger'
                    }`}
                    style={{ fontSize: '1.8rem' }}
                  >
                    {formatCurrency(totalReceitasMes - totalDespesasMes)}
                  </h3>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* Seção de Ações Rápidas e Metas - Layout melhorado */}
        <div className="row g-4 mb-5">
          <div className="col-lg-4 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderRadius: '12px' }}
            >
              <div className="card-body p-4">
                <h5 className="mb-4 text-light d-flex align-items-center">
                  <i className="bi bi-lightning-fill me-2 text-warning fs-5"></i>
                  Ações Rápidas
                </h5>
                <div className="d-grid gap-2">
                  <Link
                    to="/receitas/novo"
                    className="btn btn-success d-flex align-items-center justify-content-start py-2 px-3"
                    style={{ borderRadius: '6px' }}
                  >
                    <i className="bi bi-plus-circle-fill me-2"></i> Adicionar
                    Receita
                  </Link>
                  <Link
                    to="/despesas/novo"
                    className="btn btn-danger d-flex align-items-center justify-content-start py-2 px-3"
                    style={{ borderRadius: '6px' }}
                  >
                    <i className="bi bi-dash-circle-fill me-2"></i> Adicionar
                    Despesa
                  </Link>
                  <Link
                    to="/investimentos/novo"
                    className="btn btn-info d-flex align-items-center justify-content-start py-2 px-3"
                    style={{ borderRadius: '6px' }}
                  >
                    <i className="bi bi-graph-up me-2"></i> Adicionar
                    Investimento
                  </Link>
                  <Link
                    to="/metas/novo"
                    className="btn btn-primary d-flex align-items-center justify-content-start py-2 px-3"
                    style={{ borderRadius: '6px' }}
                  >
                    <i className="bi bi-bullseye-fill me-2"></i> Criar Nova Meta
                  </Link>
                </div>
              </div>
            </div>
          </div>

          {/* Card de metas reais - Layout melhorado */}
          <div className="col-lg-8 col-md-6">
            <div
              className="card h-100 shadow-sm"
              style={{ borderRadius: '12px' }}
            >
              <div className="card-body p-4">
                <div className="d-flex justify-content-between align-items-center mb-4">
                  <h5 className="mb-0 text-light d-flex align-items-center">
                    <i className="bi bi-rocket-takeoff-fill me-2 text-primary fs-5"></i>
                    Minhas Metas
                  </h5>
                  {metas.length > 0 && (
                    <Link
                      to="/metas"
                      className="btn btn-sm btn-outline-primary"
                    >
                      Ver Todas
                    </Link>
                  )}
                </div>
                {metas.length === 0 ? (
                  <div className="alert alert-info mb-0">
                    Você ainda não possui metas cadastradas.
                    <Link to="/metas/novo" className="alert-link ms-2">
                      Criar uma agora
                    </Link>
                  </div>
                ) : (
                  <>
                    <ul className="list-unstyled mb-0">
                      {metas.slice(0, 3).map((meta) => {
                        const currentValue = contributionsMap[meta.id] || 0;
                        const progress =
                          meta.desiredAmount > 0
                            ? Math.min(
                                (currentValue / meta.desiredAmount) * 100,
                                100
                              )
                            : 0;
                        const formatCurrency = (cents: number) => {
                          return (cents / 100).toLocaleString('pt-BR', {
                            style: 'currency',
                            currency: 'BRL',
                          });
                        };

                        return (
                          <li
                            key={meta.id}
                            className="d-flex align-items-center mb-4 pb-3"
                            style={{
                              borderBottom: '1px solid rgba(255,255,255,0.1)',
                            }}
                          >
                            <div className="me-3">
                              <i
                                className={`bi ${
                                  progress >= 100
                                    ? 'bi-check-circle-fill text-success'
                                    : 'bi-bullseye text-primary'
                                } fs-4`}
                              ></i>
                            </div>
                            <div className="flex-grow-1">
                              <h6 className="mb-2 text-light fw-bold">
                                {meta.name}
                              </h6>
                              <div
                                className="progress mb-2"
                                style={{
                                  height: '10px',
                                  backgroundColor: 'var(--color-primary-dark)',
                                  borderRadius: '5px',
                                }}
                              >
                                <div
                                  className={`progress-bar ${
                                    progress >= 100
                                      ? 'bg-success'
                                      : progress >= 50
                                      ? 'bg-info'
                                      : 'bg-warning'
                                  }`}
                                  role="progressbar"
                                  style={{
                                    width: `${progress}%`,
                                    borderRadius: '5px',
                                  }}
                                ></div>
                              </div>
                              <div className="d-flex justify-content-between align-items-center">
                                <small className="text-muted">
                                  {formatCurrency(currentValue)} de{' '}
                                  {formatCurrency(meta.desiredAmount)}
                                </small>
                                <span
                                  className={`badge ${
                                    progress >= 100
                                      ? 'bg-success'
                                      : 'bg-secondary'
                                  }`}
                                >
                                  {progress.toFixed(1)}%
                                </span>
                              </div>
                            </div>
                          </li>
                        );
                      })}
                    </ul>
                    {metas.length > 3 && (
                      <div className="mt-3 text-center">
                        <Link
                          to="/metas"
                          className="btn btn-sm btn-outline-primary"
                        >
                          Ver mais {metas.length - 3} meta(s)
                        </Link>
                      </div>
                    )}
                  </>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* Gráfico de Timeseries - Estilo Datadog */}
        <div className="row g-4 mb-5">
          <div className="col-12">
            <div className="card shadow-sm p-4">
              {/* Header com time range picker estilo Datadog */}
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0 text-light">
                  <i
                    className="bi bi-graph-up me-2"
                    style={{ color: '#7c3aed' }}
                  ></i>{' '}
                  Evolução Financeira
                </h5>
                <div className="d-flex gap-2 align-items-center">
                  {/* Quick time range buttons */}
                  <button
                    className="btn btn-sm"
                    style={{
                      backgroundColor:
                        filterByDateRange.start ===
                        new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
                          .toISOString()
                          .split('T')[0]
                          ? '#7c3aed'
                          : '#2d2d2d',
                      color: '#fff',
                      border: '1px solid #3d3d3d',
                      fontSize: '12px',
                      padding: '4px 12px',
                    }}
                    onClick={() => {
                      const end = new Date().toISOString().split('T')[0];
                      const start = new Date(
                        Date.now() - 7 * 24 * 60 * 60 * 1000
                      )
                        .toISOString()
                        .split('T')[0];
                      setFilterByDateRange({ start, end });
                    }}
                  >
                    Last 7d
                  </button>
                  <button
                    className="btn btn-sm"
                    style={{
                      backgroundColor:
                        filterByDateRange.start ===
                        new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)
                          .toISOString()
                          .split('T')[0]
                          ? '#7c3aed'
                          : '#2d2d2d',
                      color: '#fff',
                      border: '1px solid #3d3d3d',
                      fontSize: '12px',
                      padding: '4px 12px',
                    }}
                    onClick={() => {
                      const end = new Date().toISOString().split('T')[0];
                      const start = new Date(
                        Date.now() - 30 * 24 * 60 * 60 * 1000
                      )
                        .toISOString()
                        .split('T')[0];
                      setFilterByDateRange({ start, end });
                    }}
                  >
                    Last 30d
                  </button>
                  <button
                    className="btn btn-sm"
                    style={{
                      backgroundColor:
                        filterByDateRange.start ===
                        new Date(Date.now() - 90 * 24 * 60 * 60 * 1000)
                          .toISOString()
                          .split('T')[0]
                          ? '#7c3aed'
                          : '#2d2d2d',
                      color: '#fff',
                      border: '1px solid #3d3d3d',
                      fontSize: '12px',
                      padding: '4px 12px',
                    }}
                    onClick={() => {
                      const end = new Date().toISOString().split('T')[0];
                      const start = new Date(
                        Date.now() - 90 * 24 * 60 * 60 * 1000
                      )
                        .toISOString()
                        .split('T')[0];
                      setFilterByDateRange({ start, end });
                    }}
                  >
                    Last 90d
                  </button>
                  {/* Custom date range */}
                  <div className="d-flex gap-2 align-items-center ms-2">
                    <input
                      type="date"
                      className="form-control form-control-sm"
                      style={{
                        backgroundColor: '#2d2d2d',
                        border: '1px solid #3d3d3d',
                        color: '#fff',
                        fontSize: '12px',
                        width: '140px',
                      }}
                      value={filterByDateRange.start}
                      onChange={(e) =>
                        setFilterByDateRange({
                          ...filterByDateRange,
                          start: e.target.value,
                        })
                      }
                    />
                    <span className="text-muted" style={{ fontSize: '12px' }}>
                      to
                    </span>
                    <input
                      type="date"
                      className="form-control form-control-sm"
                      style={{
                        backgroundColor: '#2d2d2d',
                        border: '1px solid #3d3d3d',
                        color: '#fff',
                        fontSize: '12px',
                        width: '140px',
                      }}
                      value={filterByDateRange.end}
                      onChange={(e) =>
                        setFilterByDateRange({
                          ...filterByDateRange,
                          end: e.target.value,
                        })
                      }
                    />
                  </div>
                </div>
              </div>

              {/* Facets estilo Datadog */}
              <div className="mb-3">
                <div className="d-flex gap-2 flex-wrap align-items-center">
                  <span className="text-muted small me-2">Categoria:</span>
                  <button
                    className={`btn btn-sm ${
                      !filterByCategory ? 'active' : ''
                    }`}
                    style={{
                      backgroundColor: !filterByCategory
                        ? '#7c3aed'
                        : '#2d2d2d',
                      color: '#fff',
                      border: '1px solid #3d3d3d',
                      fontSize: '11px',
                      padding: '2px 10px',
                      borderRadius: '4px',
                    }}
                    onClick={() => setFilterByCategory('')}
                  >
                    Todas
                  </button>
                  {(() => {
                    // Combinar categorias de despesas e receitas
                    const allCategories = new Set([
                      ...Object.keys(expensesByCategory),
                      ...Object.keys(incomesByCategory),
                    ]);
                    return Array.from(allCategories)
                      .sort()
                      .map((cat) => (
                        <button
                          key={cat}
                          className={`btn btn-sm ${
                            filterByCategory === cat ? 'active' : ''
                          }`}
                          style={{
                            backgroundColor:
                              filterByCategory === cat ? '#7c3aed' : '#2d2d2d',
                            color: '#fff',
                            border: '1px solid #3d3d3d',
                            fontSize: '11px',
                            padding: '2px 10px',
                            borderRadius: '4px',
                          }}
                          onClick={() =>
                            setFilterByCategory(
                              filterByCategory === cat ? '' : cat
                            )
                          }
                        >
                          {cat}
                        </button>
                      ));
                  })()}
                </div>
              </div>

              {timeSeriesData.length === 0 ? (
                <div className="alert alert-info">
                  Nenhum dado disponível para o período selecionado.
                </div>
              ) : (
                <>
                  {/* Gráfico SVG estilo Datadog */}
                  <div
                    className="chart-container"
                    style={{
                      height: '350px',
                      position: 'relative',
                      borderRadius: '4px',
                      padding: '20px',
                    }}
                  >
                    <svg
                      width="100%"
                      height="100%"
                      viewBox="0 0 1000 320"
                      style={{ overflow: 'visible' }}
                    >
                      {(() => {
                        const filteredData = timeSeriesData.filter(
                          (d) =>
                            d.date >= filterByDateRange.start &&
                            d.date <= filterByDateRange.end
                        );
                        if (filteredData.length === 0) {
                          return (
                            <text
                              x="500"
                              y="160"
                              textAnchor="middle"
                              fill="#888"
                              fontSize="14"
                            >
                              Nenhum dado disponível para o período selecionado
                            </text>
                          );
                        }

                        const maxValue = Math.max(
                          ...filteredData.map((d) =>
                            Math.max(d.expenses, d.incomes)
                          ),
                          1
                        );
                        const minBalance = Math.min(
                          ...filteredData.map((d) => d.balance),
                          0
                        );
                        const maxBalance = Math.max(
                          ...filteredData.map((d) => d.balance),
                          0
                        );
                        const balanceRange =
                          Math.max(
                            Math.abs(maxBalance),
                            Math.abs(minBalance)
                          ) || 1;

                        const padding = 80;
                        const chartWidth = 900;
                        const chartHeight = 220;
                        const barWidth = Math.max(
                          8,
                          Math.min(20, (chartWidth / filteredData.length) * 0.7)
                        );
                        const barSpacing =
                          chartWidth / (filteredData.length || 1);
                        const baseY = padding + chartHeight;

                        // Grid lines estilo Datadog
                        const gridLines = [];
                        for (let i = 0; i <= 5; i++) {
                          const y = padding + (chartHeight / 5) * i;
                          gridLines.push(
                            <line
                              key={`grid-${i}`}
                              x1={padding}
                              y1={y}
                              x2={padding + chartWidth}
                              y2={y}
                              stroke="#2d2d2d"
                              strokeWidth="1"
                              opacity="0.5"
                            />
                          );
                        }

                        return (
                          <>
                            {/* Grid lines */}
                            {gridLines}

                            {/* Eixos */}
                            <line
                              x1={padding}
                              y1={baseY}
                              x2={padding + chartWidth}
                              y2={baseY}
                              stroke="#444"
                              strokeWidth="1.5"
                            />
                            <line
                              x1={padding}
                              y1={padding}
                              x2={padding}
                              y2={baseY}
                              stroke="#444"
                              strokeWidth="1.5"
                            />

                            {/* Y-axis labels */}
                            {[0, 1, 2, 3, 4, 5].map((i) => {
                              const value = (maxValue / 5) * (5 - i);
                              return (
                                <text
                                  key={`y-label-${i}`}
                                  x={padding - 10}
                                  y={padding + (chartHeight / 5) * i + 4}
                                  textAnchor="end"
                                  fill="#888"
                                  fontSize="11"
                                >
                                  {formatCurrency(value)}
                                </text>
                              );
                            })}

                            {/* Barras agrupadas estilo Datadog */}
                            {filteredData.map((d, i) => {
                              const x =
                                padding +
                                i * barSpacing +
                                (barSpacing - barWidth * 2 - 4) / 2;

                              // Barra de receitas
                              const incomeHeight =
                                (d.incomes / maxValue) * chartHeight;
                              const incomeY = baseY - incomeHeight;

                              // Barra de despesas
                              const expenseHeight =
                                (d.expenses / maxValue) * chartHeight;
                              const expenseY = baseY - expenseHeight;

                              return (
                                <g key={`bars-${i}`}>
                                  {/* Receitas */}
                                  <rect
                                    x={x}
                                    y={incomeY}
                                    width={barWidth}
                                    height={incomeHeight}
                                    fill="#22c55e"
                                    rx="2"
                                    style={{ cursor: 'pointer' }}
                                    onMouseEnter={(e) => {
                                      const rect = e.currentTarget;
                                      rect.setAttribute('opacity', '0.9');
                                    }}
                                    onMouseLeave={(e) => {
                                      const rect = e.currentTarget;
                                      rect.setAttribute('opacity', '1');
                                    }}
                                  >
                                    <title>{`Receitas: ${formatCurrency(
                                      d.incomes
                                    )}\n${new Date(d.date).toLocaleDateString(
                                      'pt-BR'
                                    )}`}</title>
                                  </rect>

                                  {/* Despesas */}
                                  <rect
                                    x={x + barWidth + 4}
                                    y={expenseY}
                                    width={barWidth}
                                    height={expenseHeight}
                                    fill="#ef4444"
                                    rx="2"
                                    style={{ cursor: 'pointer' }}
                                    onMouseEnter={(e) => {
                                      const rect = e.currentTarget;
                                      rect.setAttribute('opacity', '0.9');
                                    }}
                                    onMouseLeave={(e) => {
                                      const rect = e.currentTarget;
                                      rect.setAttribute('opacity', '1');
                                    }}
                                  >
                                    <title>{`Despesas: ${formatCurrency(
                                      d.expenses
                                    )}\n${new Date(d.date).toLocaleDateString(
                                      'pt-BR'
                                    )}`}</title>
                                  </rect>
                                </g>
                              );
                            })}

                            {/* Linha de saldo acumulado */}
                            <path
                              d={filteredData
                                .map((d, i) => {
                                  const x =
                                    padding + i * barSpacing + barSpacing / 2;
                                  const normalizedBalance =
                                    (d.balance + Math.abs(minBalance)) /
                                    balanceRange;
                                  const y =
                                    baseY - normalizedBalance * chartHeight;
                                  return `${i === 0 ? 'M' : 'L'} ${x} ${y}`;
                                })
                                .join(' ')}
                              fill="none"
                              stroke="#3b82f6"
                              strokeWidth="2"
                              strokeDasharray="4,4"
                              opacity="0.8"
                            />

                            {/* Labels de datas */}
                            {filteredData.map((d, i) => {
                              const x =
                                padding + i * barSpacing + barSpacing / 2;
                              const shouldShow =
                                i %
                                  Math.max(
                                    1,
                                    Math.floor(filteredData.length / 10)
                                  ) ===
                                  0 || i === filteredData.length - 1;

                              return shouldShow ? (
                                <text
                                  key={`label-${i}`}
                                  x={x}
                                  y={baseY + 18}
                                  textAnchor="middle"
                                  fill="#888"
                                  fontSize="10"
                                >
                                  {new Date(d.date).toLocaleDateString(
                                    'pt-BR',
                                    { month: 'short', day: 'numeric' }
                                  )}
                                </text>
                              ) : null;
                            })}
                          </>
                        );
                      })()}
                    </svg>
                  </div>

                  {/* Legenda estilo Datadog */}
                  <div className="d-flex gap-4 justify-content-start mt-3">
                    <div className="d-flex align-items-center">
                      <div
                        style={{
                          width: '12px',
                          height: '12px',
                          background: '#22c55e',
                          marginRight: '6px',
                          borderRadius: '2px',
                        }}
                      ></div>
                      <small
                        className="text-muted"
                        style={{ fontSize: '12px' }}
                      >
                        Receitas
                      </small>
                    </div>
                    <div className="d-flex align-items-center">
                      <div
                        style={{
                          width: '12px',
                          height: '12px',
                          background: '#ef4444',
                          marginRight: '6px',
                          borderRadius: '2px',
                        }}
                      ></div>
                      <small
                        className="text-muted"
                        style={{ fontSize: '12px' }}
                      >
                        Despesas
                      </small>
                    </div>
                    <div className="d-flex align-items-center">
                      <div
                        style={{
                          width: '20px',
                          height: '2px',
                          background: '#3b82f6',
                          borderTop: '2px dashed #3b82f6',
                          marginRight: '6px',
                        }}
                      ></div>
                      <small
                        className="text-muted"
                        style={{ fontSize: '12px' }}
                      >
                        Saldo Acumulado
                      </small>
                    </div>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>

        {/* Despesas por Categoria */}
        <div className="row g-4 mb-5">
          <div className="col-12">
            <div className="card shadow-sm p-4">
              <h5 className="mb-4 text-light">
                <i className="bi bi-tags-fill me-2 text-danger"></i> Despesas
                por Categoria
              </h5>
              {Object.keys(expensesByCategory).length === 0 ? (
                <div className="alert alert-info">
                  Nenhuma despesa categorizada ainda.
                </div>
              ) : (
                <div className="row g-3">
                  {Object.entries(expensesByCategory)
                    .sort((a, b) => b[1] - a[1])
                    .map(([category, amount]) => {
                      const totalDespesas = Object.values(
                        expensesByCategory
                      ).reduce((sum, val) => sum + val, 0);
                      const percentage =
                        totalDespesas > 0
                          ? ((amount / totalDespesas) * 100).toFixed(1)
                          : '0.0';
                      return (
                        <div key={category} className="col-md-6 col-lg-4">
                          <div className="card border-danger h-100">
                            <div className="card-body">
                              <h6 className="card-title text-danger mb-2">
                                {category}
                              </h6>
                              <h4 className="card-text text-danger fw-bold">
                                {formatCurrency(amount / 100)}
                              </h4>
                              <small className="text-muted">
                                {percentage}% do total
                              </small>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Receitas por Categoria */}
        <div className="row g-4 mb-5">
          <div className="col-12">
            <div className="card shadow-sm p-4">
              <h5 className="mb-4 text-light">
                <i className="bi bi-tags-fill me-2 text-success"></i> Receitas
                por Categoria
              </h5>
              {Object.keys(incomesByCategory).length === 0 ? (
                <div className="alert alert-info">
                  Nenhuma receita categorizada ainda.
                </div>
              ) : (
                <div className="row g-3">
                  {Object.entries(incomesByCategory)
                    .sort((a, b) => b[1] - a[1])
                    .map(([category, amount]) => {
                      const totalReceitas = Object.values(
                        incomesByCategory
                      ).reduce((sum, val) => sum + val, 0);
                      const percentage =
                        totalReceitas > 0
                          ? ((amount / totalReceitas) * 100).toFixed(1)
                          : '0.0';
                      return (
                        <div key={category} className="col-md-6 col-lg-4">
                          <div className="card border-success h-100">
                            <div className="card-body">
                              <h6 className="card-title text-success mb-2">
                                {category}
                              </h6>
                              <h4 className="card-text text-success fw-bold">
                                {formatCurrency(amount / 100)}
                              </h4>
                              <small className="text-muted">
                                {percentage}% do total
                              </small>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="mt-5">
          <h3 className="h4 border-bottom pb-2 mb-4 text-light">
            Informações da Organização
          </h3>
          <div className="p-4 rounded shadow-sm card">
            <p className="mb-1 text-light">
              <strong>{currentOrganization.name}</strong>
            </p>
            <p className="mb-0">
              <span
                className={`badge ${
                  currentOrganization.isActive ? 'bg-success' : 'bg-secondary'
                }`}
              >
                {currentOrganization.isActive
                  ? 'Status: Ativa'
                  : 'Status: Inativa'}
              </span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
