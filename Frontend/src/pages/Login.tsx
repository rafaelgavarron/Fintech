// src/pages/Login.tsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Login: React.FC = () => {
  const [email, setEmail] = useState<string>('mock@teste.com');
  const [senha, setSenha] = useState<string>('123456');
  const [erro, setErro] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [isConnecting, setIsConnecting] = useState<boolean>(false); // tela "Conectando..."
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setErro('');
    setLoading(true);

    try {
      await login(email, senha);
      setEmail('');
      setSenha('');
      navigate('/');
    } catch (err: any) {
      console.error('Erro de simulação de login:', err);
      setErro(
        err?.message ??
          'Falha no login. Verifique suas credenciais e tente novamente.'
      );
      // não navegar em caso de erro
    } finally {
      setLoading(false);
    }
  };

  const handleSocialLogin = async (provider: 'Google' | 'Facebook') => {
    setErro('');
    setLoading(true);
    setIsConnecting(true);

    try {
      // simula redirecionamento OAuth
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // simula login com credenciais MOCK; adapte conforme seu AuthContext real
      await login(`user_${provider.toLowerCase()}@mock.com`, 'oauth_token');

      // simula carregamento do site após retorno do provider
      await new Promise((resolve) => setTimeout(resolve, 800));

      // limpeza e redirecionamento só no sucesso
      setEmail('');
      setSenha('');
      navigate('/');
    } catch (err: any) {
      console.error(`Erro na simulação de login com ${provider}:`, err);
      setErro(err?.message ?? `Falha na simulação de login com ${provider}.`);
      // não navegar em caso de erro
    } finally {
      setLoading(false);
      setIsConnecting(false);
    }
  };

  // Tela "Conectando..." enquanto espera o fluxo social
  if (isConnecting) {
    return (
      <div
        className="d-flex flex-column justify-content-center align-items-center vh-100"
        style={{ backgroundColor: 'var(--main-bg)' }}
        role="status"
        aria-live="polite"
      >
        <div
          className="spinner-border text-primary mb-3"
          role="status"
          aria-hidden="true"
          style={{ width: '4rem', height: '4rem' }}
        >
          <span className="visually-hidden">Carregando...</span>
        </div>
        <h3 style={{ color: 'var(--color-text-light)' }}>Conectando...</h3>
        <p className="text-muted">Aguarde enquanto autenticamos sua conta.</p>
      </div>
    );
  }

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100"
      style={{ backgroundColor: 'var(--main-bg)' }}
    >
      <div
        className="login-container shadow-sm"
        role="region"
        aria-label="Formulário de login"
      >
        <div className="logo mb-4 d-flex align-items-center justify-content-center gap-2">
          <span
            className="logo-text"
            style={{ color: 'var(--color-brand-accent)' }}
          >
            Fintech
          </span>
        </div>

        <div className="mb-4">
          <h5
            className="text-center"
            style={{ color: 'var(--color-text-light)' }}
          >
            Bem-vindo ao Fintech!
          </h5>
          <p className="text-muted text-center">
            Gerencie suas finanças de forma rápida e prática.
          </p>
        </div>

        <form onSubmit={handleSubmit} noValidate>
          {erro && (
            <div
              className="alert alert-danger"
              role="alert"
              aria-live="assertive"
            >
              {erro}
            </div>
          )}

          <div className="mb-3">
            <label htmlFor="email" className="visually-hidden">
              E-mail
            </label>
            <input
              id="email"
              name="email"
              type="email"
              className="form-control"
              placeholder="E-mail"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              disabled={loading || isConnecting}
              autoComplete="email"
            />
          </div>

          <div className="mb-3">
            <label htmlFor="senha" className="visually-hidden">
              Senha
            </label>
            <input
              id="senha"
              name="senha"
              type="password"
              className="form-control"
              placeholder="Senha"
              required
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              disabled={loading || isConnecting}
              autoComplete="current-password"
            />
          </div>

          <div className="mb-3 text-end">
            <Link
              to="/forgot-password"
              className="text-decoration-none text-secondary-color small"
            >
              Esqueceu a senha?
            </Link>
          </div>

          <div className="d-grid mb-3">
            <button
              type="submit"
              className="btn btn-primary btn-lg"
              disabled={loading || isConnecting}
              aria-busy={loading}
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </div>

          <div className="social-login d-grid gap-2">
            <button
              className="btn btn-light w-100 d-flex align-items-center justify-content-center fw-bold"
              type="button"
              onClick={() => handleSocialLogin('Google')}
              disabled={loading || isConnecting}
              aria-label="Continuar com Google"
            >
              <img
                src="/Google.svg"
                alt=""
                className="social-icon me-2"
                style={{ width: '20px' }}
                aria-hidden="true"
              />
              Continuar com Google
            </button>

            <button
              className="btn btn-primary w-100 mt-3 d-flex align-items-center justify-content-center fw-bold"
              type="button"
              onClick={() => handleSocialLogin('Facebook')}
              disabled={loading || isConnecting}
              style={{ backgroundColor: '#1877f2', borderColor: '#1877f2' }}
              aria-label="Continuar com Facebook"
            >
              <img
                src="/Facebook.png"
                alt=""
                className="social-icon me-2"
                style={{ width: '20px' }}
                aria-hidden="true"
              />
              Continuar com Facebook
            </button>
          </div>

          <div className="text-center mt-4">
            <Link
              to="/register"
              className="text-secondary-color fw-semibold small mb-0 text-decoration-underline"
            >
              Cadastre-se aqui
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
