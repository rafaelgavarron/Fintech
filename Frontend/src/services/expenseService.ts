import { API_BASE_URL, getHeaders } from '../config/api';
import type { Expense } from '../types/expense';

export type { Expense };

export const expenseService = {
  async getAll(): Promise<Expense[]> {
    const response = await fetch(`${API_BASE_URL}/expenses`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar despesas');
    }

    return response.json();
  },

  async getById(id: string): Promise<Expense> {
    const response = await fetch(`${API_BASE_URL}/expenses/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar despesa');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<Expense[]> {
    const response = await fetch(`${API_BASE_URL}/expenses/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar despesas da organização');
    }

    return response.json();
  },

  async create(expense: {
    organizationId: string;
    targetMemberId?: string;
    targetGroupId?: string;
    name: string;
    expenseDate: number;
    expenseAmount: number;
    description?: string;
    category?: string;
  }): Promise<Expense> {
    const response = await fetch(`${API_BASE_URL}/expenses`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(expense),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar despesa');
    }

    return response.json();
  },

  async update(id: string, expense: Partial<Expense>): Promise<Expense> {
    const response = await fetch(`${API_BASE_URL}/expenses/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify(expense),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar despesa');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/expenses/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar despesa');
    }
  },

  async getTotalByOrganization(organizationId: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/expenses/organization/${organizationId}/total`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar total de despesas');
    }

    const data = await response.json();
    // Backend retorna um número direto, não um objeto
    return typeof data === 'number' ? data : (data.total || 0);
  },

  async getByCategory(organizationId: string, category: string): Promise<Expense[]> {
    const response = await fetch(`${API_BASE_URL}/expenses/organization/${organizationId}/category/${encodeURIComponent(category)}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar despesas por categoria');
    }

    return response.json();
  },

  async getTotalByCategory(organizationId: string, category: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/expenses/organization/${organizationId}/category/${encodeURIComponent(category)}/total`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar total de despesas por categoria');
    }

    const data = await response.json();
    return typeof data === 'number' ? data : (data.total || 0);
  },
};

