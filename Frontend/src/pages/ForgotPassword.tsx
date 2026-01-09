// pages/ForgotPassword.tsx

import { useState } from 'react';
import { Link } from 'react-router-dom';

function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{
    type: 'success' | 'error';
    text: string;
  } | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setLoading(true);

    try {
      // Simulação de chamada de API para envio de e-mail de recuperação
      console.log(`Simulando envio de link de recuperação para: ${email}`);
      await new Promise((resolve) => setTimeout(resolve, 1500));

      setMessage({
        type: 'success',
        text: 'Um link de recuperação de senha foi enviado para o seu e-mail.',
      });
      setEmail('');
    } catch (error: any) {
      // Em uma aplicação real, você capturaria o erro do serviço aqui.
      console.error('Erro ao solicitar recuperação de senha:', error);
      setMessage({
        type: 'error',
        text: 'Erro ao processar a solicitação. Verifique o e-mail e tente novamente.',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100"
      style={{ backgroundColor: 'var(--main-bg)' }}
    >
      <div className="login-container shadow-sm">
        <div className="logo mb-4 d-flex flex-column align-items-center justify-content-center gap-2">
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
            Recuperar Senha
          </h5>
          <p className="text-muted text-center">
            Insira seu e-mail para receber um link de redefinição.
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          {message && (
            <div
              className={`alert alert-${
                message.type === 'success' ? 'success' : 'danger'
              } mb-4`}
              role="alert"
            >
              {message.text}
            </div>
          )}

          <div className="mb-3">
            <label htmlFor="emailInput" className="form-label text-light">
              E-mail
            </label>
            <input
              id="emailInput"
              type="email"
              className="form-control"
              placeholder="Seu e-mail cadastrado"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="d-grid my-4">
            <button
              type="submit"
              className="btn btn-primary btn-lg"
              disabled={loading}
            >
              {loading ? 'Enviando...' : 'Enviar Link de Recuperação'}
            </button>
          </div>

          <div className="text-center mt-4">
            <Link
              to="/login"
              className="text-secondary-color fw-semibold small mb-0 text-decoration-underline"
            >
              Lembrou? Voltar para o Login
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ForgotPassword;
