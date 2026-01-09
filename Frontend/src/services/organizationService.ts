import { API_BASE_URL, getHeaders } from '../config/api';
import type { Organization } from '../types/organization';

export type { Organization };

export const organizationService = {
  async getAll(): Promise<Organization[]> {
    const response = await fetch(`${API_BASE_URL}/organizations`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar organizações');
    }

    return response.json();
  },

  async getActive(): Promise<Organization[]> {
    const response = await fetch(`${API_BASE_URL}/organizations/active`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar organizações ativas');
    }

    return response.json();
  },

  async getById(id: string): Promise<Organization> {
    const response = await fetch(`${API_BASE_URL}/organizations/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar organização');
    }

    return response.json();
  },

  async create(name: string, isActive: boolean = true, trialExpireAt?: number): Promise<Organization> {
    const response = await fetch(`${API_BASE_URL}/organizations`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ name, isActive, trialExpireAt: trialExpireAt || Date.now() }),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar organização');
    }

    return response.json();
  },

  async update(id: string, name: string, isActive?: boolean, trialExpireAt?: number): Promise<Organization> {
    const body: any = { name };
    if (isActive !== undefined) body.isActive = isActive;
    if (trialExpireAt !== undefined) body.trialExpireAt = trialExpireAt;

    const response = await fetch(`${API_BASE_URL}/organizations/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar organização');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/organizations/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar organização');
    }
  },
};

