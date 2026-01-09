// Função para formatar valor em dinheiro no input
export const formatCurrencyInput = (value: string): string => {
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
export const parseCurrencyValue = (formatted: string): number => {
  const numbers = formatted.replace(/\D/g, '');
  if (!numbers) return 0;
  return parseInt(numbers, 10) / 100;
};

// Função para converter valor formatado para centavos (para envio ao backend)
export const parseCurrencyToCents = (formatted: string): number => {
  const numbers = formatted.replace(/\D/g, '');
  if (!numbers) return 0;
  return parseInt(numbers, 10);
};

