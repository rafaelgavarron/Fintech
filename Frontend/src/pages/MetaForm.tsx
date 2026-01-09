import { useState, useEffect } from 'react';
import { useNavigate, Link, useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { goalService } from '../services/goalService';
import type { Goal } from '../types/goal';

function MetaForm() {
  const { currentOrganization } = useAuth();
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [desiredAmount, setDesiredAmount] = useState('');
  const [desiredAmountRaw, setDesiredAmountRaw] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const isEditing = !!id;

  // Função para formatar valor em dinheiro
  const formatCurrencyInput = (value: string): string => {
    // Remove tudo que não é dígito
    const numbers = value.replace(/\D/g, '');
    
    if (!numbers) return '';
    
    // Converte para número e divide por 100 para ter centavos
    const cents = parseInt(numbers, 10);
    const reais = cents / 100;
    
    // Formata com pontos e vírgula
    return reais.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  // Função para converter valor formatado para número
  const parseCurrencyValue = (formatted: string): number => {
    const numbers = formatted.replace(/\D/g, '');
    if (!numbers) return 0;
    return parseInt(numbers, 10) / 100;
  };

  useEffect(() => {
    if (isEditing && id) {
      loadGoal();
    }
  }, [id, isEditing]);

  const loadGoal = async () => {
    if (!id) return;

    try {
      setLoading(true);
      const goal = await goalService.getById(id);
      setName(goal.name);
      setDescription(goal.description || '');
      const valueInReais = goal.desiredAmount / 100;
      const formatted = valueInReais.toLocaleString('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      setDesiredAmount(formatted);
      setDesiredAmountRaw(valueInReais.toString());
      
      // Converter timestamp Unix para formato de data (YYYY-MM-DD)
      const date = new Date(goal.dueDate * 1000);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      setDueDate(`${year}-${month}-${day}`);
    } catch (error) {
      console.error('Erro ao carregar meta:', error);
      setError('Erro ao carregar meta');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentOrganization) return;

    setLoading(true);
    setError(null);

    try {
      // Converter valor formatado para centavos
      const desiredAmountCents = Math.round(parseCurrencyValue(desiredAmount) * 100);
      
      // Converter data para timestamp Unix (segundos)
      const dueDateTimestamp = Math.floor(new Date(dueDate).getTime() / 1000);

      if (isEditing && id) {
        // Atualizar meta existente
        await goalService.update(id, {
          name,
          description: description || undefined,
          desiredAmount: desiredAmountCents,
          dueDate: dueDateTimestamp,
        });
      } else {
        // Criar nova meta
        await goalService.create({
          organizationId: currentOrganization.id,
          name,
          description: description || undefined,
          desiredAmount: desiredAmountCents,
          dueDate: dueDateTimestamp,
        });
      }

      navigate('/metas');
    } catch (error: any) {
      console.error('Erro ao salvar meta:', error);
      setError(error.message || 'Erro ao salvar meta');
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
        <h1 className="h3 fw-bold text-primary">
          {isEditing ? 'Editar Meta' : 'Nova Meta'}
        </h1>
        <p className="text-muted mb-4">
          {isEditing ? 'Atualize os dados da sua meta financeira.' : 'Crie uma nova meta financeira para sua organização.'}
        </p>

        <div className="card shadow-sm p-4">
          <div className="card-body">
            {error && (
              <div className="alert alert-danger mb-4">{error}</div>
            )}

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="name" className="form-label text-light">
                  Nome da Meta <span className="text-danger">*</span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                  disabled={loading}
                  placeholder="Ex: Reserva de Emergência"
                />
              </div>

              <div className="mb-3">
                <label htmlFor="description" className="form-label text-light">
                  Descrição
                </label>
                <textarea
                  className="form-control"
                  id="description"
                  rows={3}
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  disabled={loading}
                  placeholder="Descreva sua meta (opcional)"
                />
              </div>

              <div className="mb-3">
                <label htmlFor="desiredAmount" className="form-label text-light">
                  Valor Desejado (R$) <span className="text-danger">*</span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="desiredAmount"
                  value={desiredAmount}
                  onChange={(e) => {
                    const formatted = formatCurrencyInput(e.target.value);
                    setDesiredAmount(formatted);
                  }}
                  required
                  disabled={loading}
                  placeholder="0,00"
                />
              </div>

              <div className="mb-4">
                <label htmlFor="dueDate" className="form-label text-light">
                  Data Limite <span className="text-danger">*</span>
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="dueDate"
                  value={dueDate}
                  onChange={(e) => setDueDate(e.target.value)}
                  required
                  disabled={loading}
                  min={new Date().toISOString().split('T')[0]}
                />
              </div>

              <div className="d-flex gap-2">
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? 'Salvando...' : isEditing ? 'Atualizar Meta' : 'Criar Meta'}
                </button>
                <Link to="/metas" className="btn btn-secondary">
                  Cancelar
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MetaForm;
