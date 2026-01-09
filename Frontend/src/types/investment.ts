// Novo arquivo para definir o tipo de dado de investimento
export interface Investment {
  id: string;
  organizationId: string;
  memberId: string;
  name: string; // Nome do ativo (ex: "Tesouro Selic 2029")
  category: string; // Categoria (ex: "Renda Fixa")
  amount: number; // Valor em centavos
  purchaseDate: number; // Data da compra (timestamp)
  description?: string;
}
