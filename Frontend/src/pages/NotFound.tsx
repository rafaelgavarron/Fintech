
import { Link } from 'react-router-dom';

function NotFound() {
  return (
    <div className="main-content">
      <div className="container-fluid text-center py-5">
        <h1 className="display-1 fw-bold text-danger">404</h1>
        <h2 className="h3 mb-3">Página Não Encontrada</h2>
        <p className="text-muted mb-4">
          O endereço que você tentou acessar não existe. Verifique a URL e tente novamente.
        </p>
        
        <Link to="/" className="btn btn-primary">
          Voltar para a Dashboard
        </Link>
      </div>
    </div>
  );
}

export default NotFound;