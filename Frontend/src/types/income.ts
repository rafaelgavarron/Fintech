export interface Income {
  id: string;
  organizationId: string;
  targetMemberId?: string;
  targetGroupId?: string;
  bankTransactionId?: string;
  name: string;
  incomeDate: number;
  incomeAmount: number;
  description?: string;
  category?: string;
}

