import { API_BASE_URL, getHeaders } from '../config/api';
import type { Member } from '../types/member';

export type { Member };

export const memberService = {
  async getAll(): Promise<Member[]> {
    const response = await fetch(`${API_BASE_URL}/members`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar membros');
    }

    return response.json();
  },

  async getById(id: string): Promise<Member> {
    const response = await fetch(`${API_BASE_URL}/members/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar membro');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<Member[]> {
    const response = await fetch(`${API_BASE_URL}/members/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar membros da organização');
    }

    return response.json();
  },

  async getByUser(userId: string): Promise<Member[]> {
    const response = await fetch(`${API_BASE_URL}/members/user/${userId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar membros do usuário');
    }

    return response.json();
  },

  async getByUserAndOrganization(userId: string, organizationId: string): Promise<Member | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/members/user/${userId}/organization/${organizationId}`, {
        headers: getHeaders(),
      });

      if (!response.ok) {
        // 404 significa que não existe, o que é OK
        if (response.status === 404) {
          return null;
        }
        // Outros erros são inesperados, mas ainda retornamos null para não quebrar o fluxo
        console.warn(`Erro ao buscar membro: ${response.status}`);
        return null;
      }

      return response.json();
    } catch (error) {
      console.warn('Erro ao buscar membro por usuário e organização:', error);
      return null;
    }
  },

  async create(organizationId: string, userId: string, roleId: string): Promise<Member> {
    try {
      const response = await fetch(`${API_BASE_URL}/members`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({ organizationId, userId, roleId }),
      });

      if (!response.ok) {
        let errorMessage = `Erro ao criar membro (${response.status})`;
        let errorDetails = '';
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorData.error || errorMessage;
          errorDetails = JSON.stringify(errorData);
        } catch {
          // Se não conseguir parsear JSON, tentar ler como texto
          try {
            errorDetails = await response.text();
          } catch {
            errorDetails = 'Sem detalhes do erro';
          }
        }
        console.error('Erro ao criar membro - Detalhes:', {
          status: response.status,
          statusText: response.statusText,
          organizationId,
          userId,
          roleId,
          errorDetails
        });
        throw new Error(errorMessage);
      }

      const data = await response.json();
      return data;
    } catch (error: any) {
      console.error('Erro no create member:', error);
      throw error;
    }
  },

  async updateRole(id: string, roleId: string): Promise<Member> {
    const response = await fetch(`${API_BASE_URL}/members/${id}/role`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify({ roleId }),
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar role do membro');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/members/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar membro');
    }
  },
};
