import { API_BASE_URL, getHeaders } from '../config/api';
import type { Goal, GoalContribution } from '../types/goal';

export type { Goal, GoalContribution };

export const goalService = {
  async getAll(): Promise<Goal[]> {
    const response = await fetch(`${API_BASE_URL}/goals`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar metas');
    }

    return response.json();
  },

  async getById(id: string): Promise<Goal> {
    const response = await fetch(`${API_BASE_URL}/goals/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar meta');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<Goal[]> {
    const response = await fetch(`${API_BASE_URL}/goals/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar metas da organização');
    }

    return response.json();
  },

  async create(goal: {
    organizationId: string;
    dueDate: number;
    name: string;
    description?: string;
    desiredAmount: number;
  }): Promise<Goal> {
    const response = await fetch(`${API_BASE_URL}/goals`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(goal),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar meta');
    }

    return response.json();
  },

  async update(id: string, goal: Partial<Goal>): Promise<Goal> {
    const response = await fetch(`${API_BASE_URL}/goals/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify(goal),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar meta');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/goals/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar meta');
    }
  },

  // Contributions
  async getContributions(goalId: string): Promise<GoalContribution[]> {
    const response = await fetch(`${API_BASE_URL}/goals-contributions/goal/${goalId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar contribuições');
    }

    return response.json();
  },

  async createContribution(contribution: Omit<GoalContribution, 'id'>): Promise<GoalContribution> {
    const response = await fetch(`${API_BASE_URL}/goals-contributions`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(contribution),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar contribuição');
    }

    return response.json();
  },

  async getContributionTotal(goalId: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/goals-contributions/goal/${goalId}/total`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      // Se não houver contribuições, retorna 0
      return 0;
    }

    const data = await response.json();
    // O backend retorna um número direto, não um objeto com total
    return typeof data === 'number' ? data : (data.total || 0);
  },
};

