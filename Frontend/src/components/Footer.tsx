// src/components/layout/Footer.tsx
export default function Footer() {
  return (
    <footer className="text-center py-3 text-sm text-muted">
      © {new Date().getFullYear()} FinPath — Todos os direitos reservados
    </footer>
  );
}
