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

