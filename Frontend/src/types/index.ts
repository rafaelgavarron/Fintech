export interface User {
  id: string;
  name: string;
  email: string;
  verified: boolean;
}

export interface Organization {
  id: string;
  name: string;
  isActive: boolean;
  createdAt: number;
  trialExpireAt?: number;
}

export interface Member {
  id: string;
  organizationId: string;
  userId: string;
  roleId: string;
}

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
}

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
}

export interface Goal {
  id: string;
  organizationId: string;
  createdAt: number;
  dueDate: number;
  name: string;
  description?: string;
  desiredAmount: number;
}

export interface GoalContribution {
  id: string;
  goalId: string;
  value: number;
  contributionDate: number;
  description?: string;
}

export interface BankAccount {
  id: string;
  organizationId: string;
  memberId: string;
  bankName: string;
  accessToken?: string;
  refreshToken?: string;
  tokenExpireAt?: number;
  isConnected: boolean;
  lastSyncAt?: number;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user: User | null;
}

export interface AuthState {
  user: User | null;
  currentOrganization: Organization | null;
  token: string | null;
  isAuthenticated: boolean;
}
