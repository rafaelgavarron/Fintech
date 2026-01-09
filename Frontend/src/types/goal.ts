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

