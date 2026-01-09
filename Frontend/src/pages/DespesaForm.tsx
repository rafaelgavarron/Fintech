// pages/DespesaForm.tsx

import { useState, useEffect, useCallback } from 'react';
import { useNavigate, Link, useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { expenseService } from '../services/expenseService';
import { bankAccountService } from '../services/bankAccountService';
import {
  formatCurrencyInput,
  parseCurrencyToCents,
} from '../utils/currencyFormatter';
import type { BankAccount } from '../types/bankAccount';

function DespesaForm() {
  const { id } = useParams<{ id?: string }>();
  const { currentOrganization, member } = useAuth();
  const [name, setName] = useState('');
  const [value, setValue] = useState('');
  const [date, setDate] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState('');
  const [customCategory, setCustomCategory] = useState('');
  const [showCustomCategory, setShowCustomCategory] = useState(false);
  const [bankAccounts, setBankAccounts] = useState<BankAccount[]>([]);
  const [selectedBankAccountId, setSelectedBankAccountId] = useState(''); // Novo estado para conta selecionada
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Categorias pré-definidas
  const categories = [
    'Alimentação',
    'Transporte',
    'Moradia',
    'Saúde',
    'Educação',
    'Lazer',
    'Roupas',
    'Tecnologia',
    'Serviços',
    'Outros',
    'Customizada',
  ];

  // Função ajustada para forçar array vazio em caso de falha
  const loadBankAccounts = useCallback(async () => {
    if (!currentOrganization) return;
    try {
      const accounts = await bankAccountService.getByOrganization(
        currentOrganization.id
      );
      setBankAccounts(accounts);
      // Define a primeira conta como selecionada por padrão, se houver
      if (accounts.length > 0 && !id) {
        setSelectedBankAccountId(accounts[0].id);
      }
    } catch (error) {
      console.error('Erro ao carregar contas bancárias. API Offline?', error);
      setBankAccounts([]); // Força array vazio em caso de falha para exibir o alerta
    }
  }, [currentOrganization, id]);

  useEffect(() => {
    if (id) {
      loadExpense();
    }
    if (currentOrganization) {
      loadBankAccounts();
    }
  }, [id, currentOrganization, loadBankAccounts]); // Adicionado loadBankAccounts como dependência

  const loadExpense = async () => {
    if (!id) return;

    try {
      setLoading(true);
      const expense = await expenseService.getById(id);
      setName(expense.name);
      const valueInReais = expense.expenseAmount / 100;
      const formatted = valueInReais.toLocaleString('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      setValue(formatted);
      setDate(new Date(expense.expenseDate * 1000).toISOString().split('T')[0]);
      setDescription(expense.description || '');
      const expenseCategory = expense.category || '';
      setCategory(expenseCategory);
      // Se a categoria não está na lista pré-definida, mostrar como customizada
      if (expenseCategory && !categories.includes(expenseCategory)) {
        setShowCustomCategory(true);
        setCustomCategory(expenseCategory);
        setCategory('Customizada');
      }
      // TO-DO: Implementar o carregamento do bankAccountId da despesa, se disponível na API
    } catch (error) {
      console.error('Erro ao carregar despesa:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!currentOrganization) {
      alert('Nenhuma organização selecionada');
      return;
    }

    if (bankAccounts.length === 0) {
      alert(
        'Você precisa cadastrar uma conta bancária antes de lançar despesas. Vá para a página de Contas Bancárias.'
      );
      return;
    }

    // Validação da Conta
    if (!selectedBankAccountId && !id) {
      alert('Selecione a conta bancária de onde sairá o dinheiro.');
      return;
    }

    setLoading(true);

    try {
      const expenseDate = Math.floor(new Date(date).getTime() / 1000);
      const expenseAmount = parseCurrencyToCents(value);

      // Processar categoria: se for "Customizada", usar customCategory; se vazio/undefined, não enviar
      let finalCategory: string | undefined = undefined;
      if (category === 'Customizada') {
        finalCategory =
          customCategory && customCategory.trim() !== ''
            ? customCategory.trim()
            : undefined;
      } else if (category && category.trim() !== '') {
        finalCategory = category.trim();
      }

      if (id) {
        await expenseService.update(id, {
          name,
          expenseDate,
          expenseAmount,
          description: description || undefined,
          category: finalCategory,
        });
      } else {
        await expenseService.create({
          organizationId: currentOrganization.id,
          targetMemberId: member?.id,
          name,
          expenseDate,
          expenseAmount,
          description: description || undefined,
          category: finalCategory,
          // Adicionando a conta bancária para criação
          bankAccountId: selectedBankAccountId,
        } as any);
      }
      navigate('/despesas');
    } catch (error: any) {
      console.error('Erro ao salvar despesa:', error);
      alert(error.message || 'Erro ao salvar despesa');
    } finally {
      setLoading(false);
    }
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
        <h1 className="h3 fw-bold text-danger">
          {id ? 'Editar Despesa' : 'Registrar nova Despesa'}
        </h1>
        <p className="text-muted mb-5">
          Preencha os dados abaixo para {id ? 'atualizar' : 'registrar'} uma
          despesa.
        </p>

        {bankAccounts.length === 0 && (
          <div className="alert alert-warning mb-4">
            Você precisa cadastrar uma conta bancária antes de lançar despesas.{' '}
            <Link to="/bank-accounts">Cadastrar conta bancária</Link>
          </div>
        )}

        <div className="card shadow-sm p-4 form-gasto">
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              {/* CAMPO DE SELEÇÃO DE CONTA BANCÁRIA */}
              {!id && bankAccounts.length > 0 && (
                <div className="mb-4">
                  <label
                    htmlFor="bankAccount"
                    className="form-label fw-bold text-light"
                  >
                    Conta de Saída
                  </label>
                  <select
                    className="form-select"
                    id="bankAccount"
                    required
                    value={selectedBankAccountId}
                    onChange={(e) => setSelectedBankAccountId(e.target.value)}
                  >
                    <option value="">Selecione uma conta</option>
                    {bankAccounts.map((account) => (
                      <option key={account.id} value={account.id}>
                        {account.bankName}
                      </option>
                    ))}
                  </select>
                </div>
              )}
              {/* FIM CAMPO DE SELEÇÃO DE CONTA BANCÁRIA */}

              <div className="mb-4">
                <label htmlFor="name" className="form-label fw-bold text-light">
                  Nome da Despesa
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg border-danger"
                  id="name"
                  placeholder="Ex: Conta de Luz, Supermercado"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>

              {/* ... (restante dos campos de valor, descrição e data) ... */}
              <div className="mb-4">
                <label
                  htmlFor="valorGasto"
                  className="form-label fw-bold text-light"
                >
                  Valor (R$)
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg border-danger"
                  id="valorGasto"
                  placeholder="0,00"
                  required
                  value={value}
                  onChange={(e) => {
                    const formatted = formatCurrencyInput(e.target.value);
                    setValue(formatted);
                  }}
                />
              </div>

              <div className="mb-4">
                <label
                  htmlFor="descricaoGasto"
                  className="form-label fw-bold text-light"
                >
                  Descrição (opcional)
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="descricaoGasto"
                  placeholder="Ex: Descrição adicional"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="mb-4">
                <label
                  htmlFor="category"
                  className="form-label fw-bold text-light"
                >
                  Categoria (opcional)
                </label>
                <select
                  className="form-select"
                  id="category"
                  value={category}
                  onChange={(e) => {
                    const selected = e.target.value;
                    setCategory(selected);
                    setShowCustomCategory(selected === 'Customizada');
                    if (selected !== 'Customizada') {
                      setCustomCategory('');
                    }
                  }}
                >
                  <option value="">Selecione uma categoria</option>
                  {categories.map((cat) => (
                    <option key={cat} value={cat}>
                      {cat}
                    </option>
                  ))}
                </select>
                {showCustomCategory && (
                  <div className="mt-2">
                    <label
                      htmlFor="customCategory"
                      className="form-label text-light small"
                    >
                      Nome da categoria customizada
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="customCategory"
                      placeholder="Digite o nome da categoria"
                      value={customCategory}
                      onChange={(e) => setCustomCategory(e.target.value)}
                      required={category === 'Customizada'}
                    />
                  </div>
                )}
              </div>

              <div className="mb-5">
                <label
                  htmlFor="dataGasto"
                  className="form-label fw-bold text-light"
                >
                  Data
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="dataGasto"
                  required
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                />
              </div>
              {/* ... (fim dos campos) ... */}

              <div className="d-grid gap-2 d-md-flex justify-content-md-end pt-3 border-top">
                <Link
                  to="/despesas"
                  className="btn btn-outline-secondary me-md-2"
                >
                  <i className="bi bi-arrow-left me-1"></i> Voltar
                </Link>

                <button
                  type="submit"
                  className="btn btn-danger"
                  disabled={loading || bankAccounts.length === 0}
                >
                  <i className="bi bi-save-fill me-2"></i>
                  {loading
                    ? 'Salvando...'
                    : id
                    ? 'Atualizar Despesa'
                    : 'Registrar Despesa'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DespesaForm;
