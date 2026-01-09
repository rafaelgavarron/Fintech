export interface Expense {
  id: string;
  organizationId: string;
  targetMemberId?: string;
  targetGroupId?: string;
  bankTransactionId?: string;
  name: string;
  expenseDate: number;
  expenseAmount: number;
  description?: string;
  category?: string;
}

