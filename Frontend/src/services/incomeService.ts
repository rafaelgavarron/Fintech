import { API_BASE_URL, getHeaders } from '../config/api';
import type { Income } from '../types/income';

export type { Income };

export const incomeService = {
  async getAll(): Promise<Income[]> {
    const response = await fetch(`${API_BASE_URL}/incomes`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar receitas');
    }

    return response.json();
  },

  async getById(id: string): Promise<Income> {
    const response = await fetch(`${API_BASE_URL}/incomes/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar receita');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<Income[]> {
    const response = await fetch(`${API_BASE_URL}/incomes/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar receitas da organização');
    }

    return response.json();
  },

  async create(income: {
    organizationId: string;
    targetMemberId?: string;
    targetGroupId?: string;
    name: string;
    incomeDate: number;
    incomeAmount: number;
    description?: string;
    category?: string;
  }): Promise<Income> {
    const response = await fetch(`${API_BASE_URL}/incomes`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(income),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar receita');
    }

    return response.json();
  },

  async update(id: string, income: Partial<Income>): Promise<Income> {
    const response = await fetch(`${API_BASE_URL}/incomes/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify(income),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar receita');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/incomes/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar receita');
    }
  },

  async getTotalByOrganization(organizationId: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/incomes/organization/${organizationId}/total`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar total de receitas');
    }

    const data = await response.json();
    // Backend retorna um número direto, não um objeto
    return typeof data === 'number' ? data : (data.total || 0);
  },
};

