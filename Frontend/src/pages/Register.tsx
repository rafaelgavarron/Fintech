import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { register } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro('');

    if (password !== confirmPassword) {
      setErro('As senhas não coincidem.');
      return;
    }

    if (password.length < 6) {
      setErro('A senha deve ter no mínimo 6 caracteres.');
      return;
    }

    setLoading(true);

    try {
      await register(name, email, password);
      navigate('/');
    } catch (error: any) {
      setErro(error.message || 'Erro ao criar conta. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100">
      <div className="login-container shadow-sm">
        <div className="logo mb-4 d-flex align-items-center justify-content-center gap-2">
          <span className="logo-text">Fintech</span>
        </div>

        <div className="mb-4">
          <h5 className="text-center">Criar Nova Conta</h5>
          <p className="text-muted text-center">
            Registre-se para começar a gerenciar suas finanças.
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          {erro && (
            <div className="alert alert-danger" role="alert">
              {erro}
            </div>
          )}

          <div className="mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Nome completo"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <input
              type="email"
              className="form-control"
              placeholder="E-mail"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <input
              type="password"
              className="form-control"
              placeholder="Senha"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <input
              type="password"
              className="form-control"
              placeholder="Confirmar senha"
              required
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>

          <div className="d-grid mb-3">
            <button type="submit" className="btn btn-login" disabled={loading}>
              {loading ? 'Criando conta...' : 'Criar Conta'}
            </button>
          </div>

          <div className="text-center mt-4">
            <Link
              to="/login"
              className="text-secondary-color fw-semibold small mb-0 text-decoration-underline"
            >
              Já tem uma conta? Faça login
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Register;

