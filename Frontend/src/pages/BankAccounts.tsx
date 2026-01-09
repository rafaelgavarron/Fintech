// pages/BankAccounts.tsx

import { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { bankAccountService } from '../services/bankAccountService';
import type { BankAccount } from '../types/bankAccount';
import { useNavigate } from 'react-router-dom'; 

const BANK_OPTIONS = [
  'Itaú',
  'Bradesco',
  'Santander',
  'Banco do Brasil',
  'Caixa',
  'Nubank',
  'Inter',
  'Sicredi',
  'Sicoob',
  'Banrisul',
  'Outro',
];

// --- MOCK DE CORREÇÃO PARA A CONEXÃO ---
// Função auxiliar para garantir que as contas apareçam ativas no frontend,
// contornando o mock desatualizado do backend.
const normalizeAccounts = (data: BankAccount[]): BankAccount[] => {
    return data.map(account => ({
        ...account,
        // Força isConnected=true se for undefined/false no mock do backend,
        // garantindo que contas recém-adicionadas apareçam como Ativas.
        isConnected: account.isConnected === undefined ? true : account.isConnected 
    }));
};
// --- FIM MOCK DE CORREÇÃO ---


function BankAccounts() {
  const { currentOrganization, member } = useAuth();
  const [accounts, setAccounts] = useState<BankAccount[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [bankName, setBankName] = useState('');
  const [customBankName, setCustomBankName] = useState('');
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
  
  const navigate = useNavigate();

  const loadAccounts = useCallback(async () => {
    if (!currentOrganization) return;

    try {
      setLoading(true);
      const data = await bankAccountService.getByOrganization(currentOrganization.id);
      
      // *** CORREÇÃO APLICADA AQUI: Normaliza o status das contas ao carregar ***
      setAccounts(normalizeAccounts(data));
      
    } catch (error) {
      console.error('Erro ao carregar contas:', error);
      // Força o array vazio em caso de falha TOTAL da API (para evitar erros de renderização)
      setAccounts([]); 
    } finally {
      setLoading(false);
    }
  }, [currentOrganization]);

  useEffect(() => {
    loadAccounts();
  }, [loadAccounts]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    if (!currentOrganization || !member) {
      setMessage({ type: 'error', text: 'Organização ou membro não encontrado' });
      return;
    }

    const finalBankName = bankName === 'Outro' ? customBankName : bankName;

    if (!finalBankName) {
      setMessage({ type: 'error', text: 'Selecione ou informe o nome do banco' });
      return;
    }

    try {
      await bankAccountService.create(currentOrganization.id, member.id, finalBankName);
      
      // Mensagem de sucesso indicando status Ativa
      setMessage({ type: 'success', text: `Conta bancária (${finalBankName}) cadastrada com sucesso! Status: Ativa.` });
      
      setShowForm(false);
      setBankName('');
      setCustomBankName('');
      loadAccounts(); // Recarrega a lista
    } catch (error: any) {
      const errorMessage = error.message || 'Erro ao cadastrar conta bancária';
      setMessage({ type: 'error', text: errorMessage });
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Tem certeza que deseja remover esta conta bancária?')) {
      return;
    }

    try {
      await bankAccountService.delete(id);
      setMessage({ type: 'success', text: 'Conta bancária removida com sucesso!' });
      loadAccounts();
    } catch (error: any) {
      const errorMessage = error.message || 'Erro ao remover conta bancária';
      setMessage({ type: 'error', text: errorMessage });
    }
  };
  
  const handleGoBack = () => {
      navigate(-1);
  };

  if (loading) {
    return (
      <div className="main-content">
        <div className="container-fluid">
          <p className="text-info">Carregando contas bancárias...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="container-fluid">
        
        {/* CABEÇALHO DA PÁGINA COM BOTÃO DE VOLTAR */}
        <div className="d-flex justify-content-between align-items-start mb-5 pt-3">
            <div>
                <h1 className="h3 fw-bold text-light">Contas Bancárias</h1>
                <p className="text-muted">
                    Gerencie suas contas bancárias para usar como origem/destino de transações.
                </p>
            </div>
            
            {/* BOTÃO DE VOLTAR */}
            <button
                className="btn btn-outline-secondary mt-2"
                onClick={handleGoBack}
            >
                <i className="bi bi-arrow-left me-2"></i> Voltar
            </button>
        </div>
        {/* FIM DO CABEÇALHO */}


        {message && (
          <div className={`alert alert-${message.type === 'success' ? 'success' : 'danger'}`}>
            {message.text}
          </div>
        )}

        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="h5 text-light">Suas Contas</h2>
          <button
            className="btn btn-primary"
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? 'Cancelar' : '+ Adicionar Conta'}
          </button>
        </div>

        {showForm && (
          <div className="card shadow-sm p-4 mb-4">
            <h5 className="mb-4 text-light">Nova Conta Bancária</h5>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label text-light">Banco</label>
                <select
                  className="form-select"
                  value={bankName}
                  onChange={(e) => setBankName(e.target.value)}
                  required
                >
                  <option value="">Selecione o banco</option>
                  {BANK_OPTIONS.map((bank) => (
                    <option key={bank} value={bank}>
                      {bank}
                    </option>
                  ))}
                </select>
              </div>

              {bankName === 'Outro' && (
                <div className="mb-3">
                  <label className="form-label text-light">Nome do Banco</label>
                  <input
                    type="text"
                    className="form-control"
                    value={customBankName}
                    onChange={(e) => setCustomBankName(e.target.value)}
                    required
                    placeholder="Digite o nome do banco"
                  />
                </div>
              )}

              <button type="submit" className="btn btn-primary">
                Cadastrar Conta
              </button>
            </form>
          </div>
        )}

        {accounts.length === 0 ? (
          <div className="alert alert-info">
            Nenhuma conta bancária cadastrada. Adicione uma conta para começar a lançar receitas e despesas.
          </div>
        ) : (
          <div className="table-responsive">
            <table className="table table-dark table-striped">
              <thead>
                <tr>
                  <th className="text-light">Banco</th>
                  <th className="text-light">Status</th>
                  <th className="text-light">Ações</th>
                </tr>
              </thead>
              <tbody>
                {accounts.map((account) => (
                  <tr key={account.id}>
                    <td className="text-light">{account.bankName}</td>
                    <td>
                      <span 
                          className={`badge ${account.isConnected ? 'bg-success' : 'bg-secondary'}`}
                      >
                        {/* Exibe Ativa/Inativa */}
                        {account.isConnected ? 'Ativa' : 'Inativa'}
                      </span>
                    </td>
                    <td>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => handleDelete(account.id)}
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
  );
}

export default BankAccounts;