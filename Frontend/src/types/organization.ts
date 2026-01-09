export interface Organization {
  id: string;
  name: string;
  isActive: boolean;
  createdAt: number;
  trialExpireAt?: number;
}

