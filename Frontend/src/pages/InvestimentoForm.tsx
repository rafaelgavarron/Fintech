import { useState, useEffect } from 'react';
import { useNavigate, Link, useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { investmentService } from '../services/investmentService';
import { formatCurrencyInput, parseCurrencyToCents } from '../utils/currencyFormatter';

const CATEGORIAS_INVESTIMENTO = [
  'Renda Fixa',
  'Ações',
  'Fundos Imobiliários (FIIs)',
  'Criptomoedas',
  'Fundos de Investimento',
  'Outro',
];

function InvestimentoForm() {
  const { id } = useParams<{ id?: string }>();
  const { currentOrganization, member } = useAuth();

  const [name, setName] = useState('');
  const [category, setCategory] = useState(CATEGORIAS_INVESTIMENTO[0]);
  const [amount, setAmount] = useState('');
  const [date, setDate] = useState(new Date().toISOString().split('T')[0]);
  const [description, setDescription] = useState('');

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{
    type: 'success' | 'error';
    text: string;
  } | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      loadInvestment();
    }
  }, [id]);

  const loadInvestment = async () => {
    if (!id) return;

    try {
      setLoading(true);
      const investment = await investmentService.getById(id);
      setName(investment.name);
      setCategory(investment.category || CATEGORIAS_INVESTIMENTO[0]);
      const amountInReais = investment.amount / 100;
      const formatted = amountInReais.toLocaleString('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      setAmount(formatted);
      setDate(new Date(investment.purchaseDate * 1000).toISOString().split('T')[0]);
      setDescription(investment.description || '');
    } catch (error) {
      console.error('Erro ao carregar investimento:', error);
      setMessage({ type: 'error', text: 'Erro ao carregar investimento' });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    if (!currentOrganization || !member) {
      setMessage({
        type: 'error',
        text: 'Organização ou usuário não autenticado.',
      });
      return;
    }

    if (parseFloat(amount) <= 0) {
      setMessage({ type: 'error', text: 'O valor deve ser maior que zero.' });
      return;
    }

    setLoading(true);

    try {
      const purchaseDate = Math.floor(new Date(date).getTime() / 1000);
      const amountInCents = parseCurrencyToCents(amount);

      if (id) {
        await investmentService.update(id, {
          name,
          category,
          amount: amountInCents,
          purchaseDate,
          description: description || undefined,
        });
      } else {
        await investmentService.create({
          organizationId: currentOrganization.id,
          memberId: member.id,
          name,
          category,
          amount: amountInCents,
          purchaseDate,
          description: description || undefined,
        });
      }

      navigate('/investimentos');
    } catch (error: any) {
      console.error('Erro ao salvar investimento:', error);
      setMessage({
        type: 'error',
        text: error.message || 'Erro ao salvar investimento',
      });
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
        <h1 className="h3 fw-bold text-success">
          {id ? 'Editar Investimento' : 'Adicionar Novo Investimento'}
        </h1>
        <p className="text-muted mb-5">
          Registre um novo ativo na sua carteira.
        </p>

        {message && (
          <div className={`alert alert-${message.type} mb-4`}>
            {message.text}
          </div>
        )}

        <div className="card shadow-sm p-4">
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label htmlFor="name" className="form-label fw-bold text-light">
                  Nome do Ativo
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg border-success"
                  id="name"
                  placeholder="Ex: Tesouro Selic 2029, Ações PETR4"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>

              <div className="mb-4">
                <label
                  htmlFor="category"
                  className="form-label fw-bold text-light"
                >
                  Categoria
                </label>
                <select
                  className="form-select form-control-lg border-success"
                  id="category"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  required
                >
                  {CATEGORIAS_INVESTIMENTO.map((cat) => (
                    <option key={cat} value={cat}>
                      {cat}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-4">
                <label
                  htmlFor="amount"
                  className="form-label fw-bold text-light"
                >
                  Valor Investido (R$)
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg border-success"
                  id="amount"
                  placeholder="0,00"
                  required
                  value={amount}
                  onChange={(e) => {
                    const formatted = formatCurrencyInput(e.target.value);
                    setAmount(formatted);
                  }}
                />
              </div>

              <div className="mb-4">
                <label htmlFor="date" className="form-label fw-bold text-light">
                  Data da Aplicação/Compra
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="date"
                  required
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                />
              </div>

              <div className="mb-5">
                <label
                  htmlFor="description"
                  className="form-label fw-bold text-light"
                >
                  Descrição (opcional)
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="description"
                  placeholder="Ex: Corretora X, aporte mensal"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="d-grid gap-2 d-md-flex justify-content-md-end pt-3">
                <Link
                  to="/investimentos"
                  className="btn btn-outline-secondary me-md-2"
                >
                  <i className="bi bi-arrow-left me-1"></i> Voltar
                </Link>

                <button
                  type="submit"
                  className="btn btn-success"
                  disabled={loading}
                >
                  <i className="bi bi-save-fill me-2"></i>
                  {loading
                    ? 'Salvando...'
                    : id
                    ? 'Atualizar Investimento'
                    : 'Salvar Investimento'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default InvestimentoForm;
