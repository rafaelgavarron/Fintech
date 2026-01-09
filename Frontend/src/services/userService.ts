import { API_BASE_URL, getHeaders } from '../config/api';
import type { User, LoginResponse } from '../types/user';

export type { User, LoginResponse };

export const userService = {
  async register(name: string, email: string, password: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/users/register`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ name, email, password }),
    });

    if (!response.ok) {
      throw new Error('Erro ao registrar usuário');
    }

    return response.json();
  },

  async login(email: string, password: string): Promise<LoginResponse> {
    try {
      const response = await fetch(`${API_BASE_URL}/users/login`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({ email, password }),
      });

      let data;
      try {
        data = await response.json();
      } catch (e) {
        // Se não conseguir parsear JSON, criar um objeto básico
        data = { success: false, message: 'Resposta inválida do servidor', user: null };
      }

      console.log('Resposta completa do servidor:', {
        status: response.status,
        ok: response.ok,
        data: data
      });

      // O backend retorna 200 mesmo quando success é false
      if (!response.ok || (data.success === false)) {
        console.error('Erro HTTP:', response.status, data);
        throw new Error(data.message || 'Credenciais inválidas');
      }

      return data;
    } catch (error: any) {
      console.error('Erro no fetch/login:', error);
      throw error;
    }
  },

  async getUserById(id: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar usuário');
    }

    return response.json();
  },

  async getUserByEmail(email: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/users/email/${email}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar usuário');
    }

    return response.json();
  },

  async updateUser(id: string, name?: string, password?: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify({ name, password }),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar usuário');
    }

    return response.json();
  },
};

