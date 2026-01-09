import { API_BASE_URL, getHeaders } from '../config/api';
import type { BankAccount } from '../types/bankAccount';

export type { BankAccount };

export const bankAccountService = {
  async getAll(): Promise<BankAccount[]> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar contas bancárias');
    }

    return response.json();
  },

  async getById(id: string): Promise<BankAccount> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts/${id}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar conta bancária');
    }

    return response.json();
  },

  async getByOrganization(organizationId: string): Promise<BankAccount[]> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts/organization/${organizationId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar contas da organização');
    }

    return response.json();
  },

  async getByMember(memberId: string): Promise<BankAccount[]> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts/member/${memberId}`, {
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar contas do membro');
    }

    return response.json();
  },

  async create(
    organizationId: string,
    memberId: string,
    bankName: string,
    accessToken?: string
  ): Promise<BankAccount> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts/connect`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({
        organizationId,
        memberId,
        bankName,
        accessToken: accessToken || 'dummy-token',
      }),
    });

    if (!response.ok) {
      throw new Error('Erro ao criar conta bancária');
    }

    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/bank-accounts/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar conta bancária');
    }
  },
};

