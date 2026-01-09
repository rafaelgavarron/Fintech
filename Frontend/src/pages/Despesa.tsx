// pages/Despesa.tsx (Página Unificada de Transações)

import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { expenseService } from '../services/expenseService';
import { incomeService } from '../services/incomeService';
import { bankAccountService } from '../services/bankAccountService';
import type { BankAccount } from '../types/bankAccount'; 

// Define um tipo unificado para as transações
interface Transaction {
    id: string;
    type: 'income' | 'expense';
    name: string;
    amount: number; // Em centavos
    date: number; // Timestamp
    description: string;
    category?: string;
}

function Despesa() {
    const { currentOrganization } = useAuth();
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [bankAccounts, setBankAccounts] = useState<BankAccount[]>([]);
    
    const [loading, setLoading] = useState(true);
    const [loadingAccounts, setLoadingAccounts] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Função para carregar Contas Bancárias
    const loadBankAccounts = useCallback(async () => {
        if (!currentOrganization) return;
        try {
            setLoadingAccounts(true);
            const accounts = await bankAccountService.getByOrganization(currentOrganization.id);
            setBankAccounts(accounts);
        } catch (err) {
            console.error('Erro ao carregar contas bancárias. API Offline?', err);
            setBankAccounts([]); 
        } finally {
            setLoadingAccounts(false);
        }
    }, [currentOrganization]);


    // Função para carregar TODAS as transações
    const loadTransactions = useCallback(async () => {
        if (!currentOrganization) return;

        if (bankAccounts.length === 0) {
            setTransactions([]);
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError(null);
            
            const expenses = await expenseService.getByOrganization(currentOrganization.id);
            const expenseTransactions: Transaction[] = expenses.map(e => ({
                id: e.id, type: 'expense', name: e.name, amount: e.expenseAmount, date: e.expenseDate, description: e.description || '', category: e.category,
            }));

            const incomes = await incomeService.getByOrganization(currentOrganization.id);
            const incomeTransactions: Transaction[] = incomes.map(i => ({
                id: i.id, type: 'income', name: i.name, amount: i.incomeAmount, date: i.incomeDate, description: i.description || '', category: i.category,
            }));

            const combinedTransactions = [...expenseTransactions, ...incomeTransactions]
                .sort((a, b) => b.date - a.date); 

            setTransactions(combinedTransactions);

        } catch (err: any) {
            console.error('Erro ao carregar transações:', err);
            setError('Erro ao carregar transações. Verifique a conexão com a API.');
        } finally {
            setLoading(false);
        }
    }, [currentOrganization, bankAccounts.length]); 

    useEffect(() => {
        loadBankAccounts();
    }, [loadBankAccounts]);

    useEffect(() => {
        if (!loadingAccounts) {
            loadTransactions();
        }
    }, [loadingAccounts, loadTransactions]); 

    // Função para Excluir
    const handleDelete = async (id: string, type: 'income' | 'expense') => {
        if (!window.confirm(`Tem certeza que deseja remover ${type === 'income' ? 'esta Receita' : 'esta Despesa'}?`)) {
            return;
        }

        try {
            if (type === 'expense') {
                await expenseService.delete(id);
            } else {
                await incomeService.delete(id);
            }
            loadTransactions(); 
        } catch (err) {
            console.error('Erro ao deletar:', err);
            alert(`Erro ao deletar ${type}.`);
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
    
    // PRIORITY 1: Tela de Carregamento da Conta Bancária
    if (loadingAccounts) {
         return (
            <div className="main-content">
                <div className="container-fluid d-flex justify-content-center align-items-center" style={{ minHeight: '70vh' }}>
                    <p className="text-info">Verificando contas bancárias...</p>
                </div>
            </div>
        );
    }

    const hasBankAccounts = bankAccounts.length > 0;

    // PRIORITY 2: Bloqueio Total se Não Houver Contas
    if (!hasBankAccounts) {
        return (
            <div className="main-content">
                <div className="container-fluid d-flex flex-column align-items-center justify-content-center" style={{ minHeight: '70vh' }}>
                    <div className="card shadow-sm p-5 text-center" style={{ maxWidth: '600px' }}>
                        <i className="bi bi-bank2 fs-1 text-warning mb-3"></i>
                        <h2 className="h3 fw-bold text-light mb-3">Conta Bancária Necessária</h2>
                        <p className="text-muted mb-4">
                            Para registrar qualquer **Receita** ou **Despesa**, você precisa ter pelo menos uma conta bancária cadastrada na sua organização.
                        </p>
                        
                        <Link to="/bank-accounts" className="btn btn-primary btn-lg fw-bold">
                            <i className="bi bi-plus-circle-fill me-2"></i> Adicionar Conta Bancária
                        </Link>
                    </div>
                </div>
            </div>
        );
    }
    
    // PRIORITY 3: RENDERIZAÇÃO NORMAL (Listagem de Transações)
    return (
        <div className="main-content">
            <div className="container-fluid">
                <h1 className="h3 fw-bold text-light mb-4">Listagem de Transações</h1>
                <p className="text-muted mb-4">Receitas e Despesas da sua organização, ordenadas por data.</p>
                
                {error && <div className="alert alert-danger">{error}</div>}

                {/* Botões de Ação e Atalho para Configurações Bancárias */}
                <div className="d-flex gap-3 mb-4 flex-wrap">
                    {/* 1. Botão de Registro de Despesa */}
                    <Link to="/despesas/novo" className="btn btn-danger">
                        <i className="bi bi-dash-circle-fill me-2"></i> Registrar Despesa
                    </Link>
                    
                    {/* 2. Botão de Registro de Receita */}
                    <Link to="/receitas/novo" className="btn btn-success">
                        <i className="bi bi-plus-circle-fill me-2"></i> Registrar Receita
                    </Link>

                    {/* 3. NOVO BOTÃO: Atalho para Configurações Bancárias */}
                    <Link to="/bank-accounts" className="btn btn-outline-secondary ms-auto">
                        <i className="bi bi-bank me-2"></i> Gerenciar Contas
                    </Link>
                </div>
                
                {loading ? (
                    <p>Carregando transações...</p>
                ) : transactions.length === 0 ? (
                     <div className="card shadow-sm p-4">
                        <p className="text-center text-muted mb-0">Nenhuma transação registrada ainda.</p>
                     </div>
                ) : (
                    <div className="card shadow-sm" style={{ backgroundColor: 'var(--color-secondary-dark)', border: '1px solid #374151', borderRadius: '12px', padding: '1.5rem' }}>
                        <h5 className="mb-4 text-light" style={{ fontFamily: 'Inter, sans-serif', fontWeight: 600, fontSize: '1.5rem' }}>Transações</h5>
                        <div className="table-responsive">
                            <table className="table" style={{ marginBottom: 0 }}>
                                <thead>
                                    <tr>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Tipo</th>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Nome</th>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Valor</th>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Categoria</th>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Data</th>
                                        <th style={{ color: 'var(--color-text-muted)', fontWeight: 700, fontSize: '0.85rem', textTransform: 'uppercase', borderTop: '1px solid #374151', borderBottom: '2px solid #374151', padding: '0.75rem 0.5rem', backgroundColor: 'var(--color-secondary-dark)' }}>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {transactions.map((t) => (
                                        <tr key={t.type + t.id}>
                                            <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>
                                                <span className={`badge bg-${t.type === 'income' ? 'success' : 'danger'} fw-bold`}>
                                                    {t.type === 'income' ? 'Receita' : 'Despesa'}
                                                </span>
                                            </td>
                                            <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }} className="fw-bold">{t.name}</td>
                                            <td className={`fw-bold text-${t.type === 'income' ? 'success' : 'danger'}`} style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', padding: '1rem 0.5rem' }}>
                                                {formatCurrency(t.amount)}
                                            </td>
                                            <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>
                                                <span className="badge bg-secondary">
                                                    {t.category || 'Sem categoria'}
                                                </span>
                                            </td>
                                            <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>{formatDate(t.date)}</td>
                                            <td style={{ backgroundColor: 'var(--color-secondary-dark)', borderBottom: '1px solid #374151', color: 'var(--color-text-light)', fontWeight: 500, padding: '1rem 0.5rem' }}>
                                                <Link
                                                    to={t.type === 'expense' ? `/despesas/editar/${t.id}` : `/receitas/editar/${t.id}`}
                                                    className="btn btn-sm btn-info me-2"
                                                >
                                                    <i className="bi bi-pencil"></i> Editar
                                                </Link>
                                                <button
                                                    className="btn btn-sm btn-danger"
                                                    onClick={() => handleDelete(t.id, t.type)}
                                                >
                                                    <i className="bi bi-trash"></i> Excluir
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Despesa;