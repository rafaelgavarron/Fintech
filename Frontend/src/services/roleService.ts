import { API_BASE_URL, getHeaders } from '../config/api';

export interface Role {
  id: string;
  name: string;
  description?: string;
}

export const roleService = {
  async getAll(): Promise<Role[]> {
    const response = await fetch(`${API_BASE_URL}/roles`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar roles');
    }

    return response.json();
  },

  async getById(id: string): Promise<Role> {
    const response = await fetch(`${API_BASE_URL}/roles/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar role');
    }

    return response.json();
  },

  async getByName(name: string): Promise<Role> {
    const response = await fetch(`${API_BASE_URL}/roles/name/${name}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar role');
    }

    return response.json();
  },

  async create(name: string, description?: string): Promise<Role> {
    const response = await fetch(`${API_BASE_URL}/roles`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ name, description }),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar role');
    }

    return response.json();
  },
};

