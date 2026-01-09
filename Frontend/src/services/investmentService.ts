import { API_BASE_URL, getHeaders } from '../config/api';
import type { Investment } from '../types/investment';

export type { Investment };

export const investmentService = {
  async getAll(): Promise<Investment[]> {
    const response = await fetch(`${API_BASE_URL}/investments`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar investimentos');
    }

    return response.json();
  },

  async getById(id: string): Promise<Investment> {
    const response = await fetch(`${API_BASE_URL}/investments/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar investimento');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<Investment[]> {
    const response = await fetch(`${API_BASE_URL}/investments/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar investimentos da organização');
    }

    return response.json();
  },

  async getByMember(memberId: string): Promise<Investment[]> {
    const response = await fetch(`${API_BASE_URL}/investments/member/${memberId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar investimentos do membro');
    }

    return response.json();
  },

  async getTotalByOrganization(organizationId: string): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/investments/organization/${organizationId}/total`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar total de investimentos');
    }

    const data = await response.json();
    return typeof data === 'number' ? data : (data.total || 0);
  },

  async create(investment: {
    organizationId: string;
    memberId: string;
    name: string;
    category?: string;
    amount: number;
    purchaseDate: number;
    description?: string;
  }): Promise<Investment> {
    const response = await fetch(`${API_BASE_URL}/investments`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(investment),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar investimento');
    }

    return response.json();
  },

  async update(id: string, investment: Partial<Investment>): Promise<Investment> {
    const response = await fetch(`${API_BASE_URL}/investments/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify(investment),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar investimento');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/investments/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar investimento');
    }
  },
};
