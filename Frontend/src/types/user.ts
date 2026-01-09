export interface User {
  id: string;
  name: string;
  email: string;
  verified: boolean;
  passwordHash?: string; // Campo opcional caso venha do backend
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user: User | null;
}

