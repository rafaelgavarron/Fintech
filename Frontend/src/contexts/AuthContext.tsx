// contexts/AuthContext.tsx
import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  type ReactNode,
} from 'react';
import { userService } from '../services/userService';
import { organizationService } from '../services/organizationService';
import { memberService } from '../services/memberService';
import { roleService } from '../services/roleService';
import type { User } from '../types/user';
import type { Organization } from '../types/organization';
import type { Member } from '../types/member';

interface AuthContextType {
  user: User | null;
  currentOrganization: Organization | null;
  userOrganizations: Organization[];
  member: Member | null;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  setCurrentOrganization: (org: Organization) => void;
  refreshUserOrganizations: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [currentOrganization, setCurrentOrganization] =
    useState<Organization | null>(null);
  const [userOrganizations, setUserOrganizations] = useState<Organization[]>(
    []
  );
  const [member, setMember] = useState<Member | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Carregar estado do localStorage ao iniciar
  useEffect(() => {
    const loadAuthState = async () => {
      try {
        const savedUser = localStorage.getItem('user');
        const savedOrgId = localStorage.getItem('currentOrganizationId');

        if (savedUser) {
          try {
            const user: User = JSON.parse(savedUser);
            setUser(user);

            // Carregar organizações do usuário através dos membros
            const members = await memberService.getByUser(user.id);

            if (members.length > 0) {
              // Buscar detalhes das organizações
              const orgPromises = members.map((m) =>
                organizationService.getById(m.organizationId)
              );
              const organizations = await Promise.all(orgPromises);

              setUserOrganizations(organizations);

              // Se houver uma organização salva, carregá-la
              if (savedOrgId) {
                const savedOrg = organizations.find((o) => o.id === savedOrgId);
                if (savedOrg) {
                  setCurrentOrganization(savedOrg);
                  const userMember = members.find(
                    (m) => m.organizationId === savedOrgId
                  );
                  if (userMember) {
                    setMember(userMember);
                  }
                } else if (organizations.length > 0) {
                  // Se a organização salva não existir mais, usar a primeira disponível
                  setCurrentOrganization(organizations[0]);
                  setMember(members[0]);
                  localStorage.setItem(
                    'currentOrganizationId',
                    organizations[0].id
                  );
                }
              } else if (organizations.length > 0) {
                // Se não houver organização salva, usar a primeira
                setCurrentOrganization(organizations[0]);
                setMember(members[0]);
                localStorage.setItem(
                  'currentOrganizationId',
                  organizations[0].id
                );
              }
            }
          } catch (error) {
            console.error('Erro ao carregar dados do usuário:', error);
            // Se houver erro, limpar localStorage e considerar deslogado
            localStorage.removeItem('user');
            localStorage.removeItem('currentOrganizationId');
            setUser(null);
          }
        } else {
          // Se não houver dados, o usuário é considerado deslogado
          setUser(null);
        }
      } catch (error) {
        console.error('Erro ao carregar estado de autenticação:', error);
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    loadAuthState();
  }, []);

  const refreshUserOrganizations = async (userId?: string) => {
    if (!user && !userId) {
      console.warn('Nenhum usuário disponível para atualizar organizações');
      return;
    }

    const targetUserId = userId || user?.id;
    if (!targetUserId) return;

    try {
      const members = await memberService.getByUser(targetUserId);

      if (members.length > 0) {
        const orgPromises = members.map((m) =>
          organizationService.getById(m.organizationId)
        );
        const organizations = await Promise.all(orgPromises);
        setUserOrganizations(organizations);
      } else {
        setUserOrganizations([]);
      }
    } catch (error) {
      console.error('Erro ao atualizar organizações:', error);
    }
  };

  const login = async (email: string, password: string) => {
    // --- MOCK DE LOGIN (Bypass Backend) ---
    // Credenciais para teste: mock@teste.com / 123456
    if (email === 'mock@teste.com' && password === '123456') {
      const mockUser: any = {
        id: 'user-mock-123',
        name: 'Usuário Mock',
        email: 'mock@teste.com',
      };

      const mockOrg: any = {
        id: 'org-mock-123',
        name: 'Organização Mock',
        isActive: true,
      };

      const mockMember: any = {
        id: 'member-mock-123',
        userId: 'user-mock-123',
        organizationId: 'org-mock-123',
        roleId: 'role-admin',
      };

      setUser(mockUser);
      localStorage.setItem('user', JSON.stringify(mockUser));

      setUserOrganizations([mockOrg]);
      setCurrentOrganization(mockOrg);
      setMember(mockMember);
      localStorage.setItem('currentOrganizationId', mockOrg.id);
      return;
    }
    // --------------------------------------

    try {
      const loginResponse = await userService.login(email, password);

      if (!loginResponse.success || !loginResponse.user) {
        throw new Error(loginResponse.message || 'Credenciais inválidas');
      }

      const loggedUser = loginResponse.user;
      setUser(loggedUser);
      localStorage.setItem('user', JSON.stringify(loggedUser));

      // Carregar organizações do usuário através dos membros
      const members = await memberService.getByUser(loggedUser.id);

      if (members.length > 0) {
        // Buscar detalhes das organizações
        const orgPromises = members.map((m) =>
          organizationService.getById(m.organizationId)
        );
        const organizations = await Promise.all(orgPromises);

        setUserOrganizations(organizations);

        // Usar a primeira organização como padrão
        const firstOrg = organizations[0];
        const firstMember = members[0];

        setCurrentOrganization(firstOrg);
        setMember(firstMember);
        localStorage.setItem('currentOrganizationId', firstOrg.id);
      } else {
        // Se o usuário não tiver organizações, criar uma padrão
        try {
          // Buscar role "Owner" ou criar uma organização padrão
          const roles = await roleService.getAll();
          const ownerRole = roles.find((r) => r.name === 'Owner') || roles[0];

          if (!ownerRole) {
            throw new Error('Nenhum role disponível no sistema');
          }

          // Criar organização padrão para o usuário
          const defaultOrg = await organizationService.create(
            `${loggedUser.name}'s Organization`,
            true,
            Math.floor(Date.now() / 1000) + 90 * 24 * 60 * 60 // 90 dias de trial
          );

          // Criar membro associando usuário à organização
          const newMember = await memberService.create(
            defaultOrg.id,
            loggedUser.id,
            ownerRole.id
          );

          setUserOrganizations([defaultOrg]);
          setCurrentOrganization(defaultOrg);
          setMember(newMember);
          localStorage.setItem('currentOrganizationId', defaultOrg.id);
        } catch (error) {
          console.error('Erro ao criar organização padrão:', error);
          setUserOrganizations([]);
        }
      }
    } catch (error: any) {
      console.error('Erro no login:', error);
      throw error;
    }
  };

  const register = async (name: string, email: string, password: string) => {
    try {
      // Registrar usuário
      const newUser = await userService.register(name, email, password);
      setUser(newUser);
      localStorage.setItem('user', JSON.stringify(newUser));

      // Buscar role "Owner" ou usar o primeiro disponível
      const roles = await roleService.getAll();
      const ownerRole = roles.find((r) => r.name === 'Owner') || roles[0];

      if (!ownerRole) {
        throw new Error('Nenhum role disponível no sistema');
      }

      // Criar organização padrão para o novo usuário
      const defaultOrg = await organizationService.create(
        `${name}'s Organization`,
        true,
        Math.floor(Date.now() / 1000) + 90 * 24 * 60 * 60 // 90 dias de trial
      );

      // Criar membro associando usuário à organização
      const newMember = await memberService.create(
        defaultOrg.id,
        newUser.id,
        ownerRole.id
      );

      setUserOrganizations([defaultOrg]);
      setCurrentOrganization(defaultOrg);
      setMember(newMember);
      localStorage.setItem('currentOrganizationId', defaultOrg.id);
    } catch (error: any) {
      console.error('Erro no registro:', error);
      throw error;
    }
  };

  const logout = () => {
    setUser(null);
    setCurrentOrganization(null);
    setUserOrganizations([]);
    setMember(null);
    localStorage.removeItem('user');
    localStorage.removeItem('currentOrganizationId');
  };

  const handleSetCurrentOrganization = async (org: Organization) => {
    if (!user) return;

    try {
      // Buscar membro do usuário na organização selecionada
      const memberData = await memberService.getByUserAndOrganization(
        user.id,
        org.id
      );

      if (memberData) {
        setCurrentOrganization(org);
        setMember(memberData);
        localStorage.setItem('currentOrganizationId', org.id);
      } else {
        console.warn(`Usuário não é membro da organização ${org.id}`);
        // Mesmo assim, atualizar a organização (pode ser um caso especial)
        setCurrentOrganization(org);
        localStorage.setItem('currentOrganizationId', org.id);
      }
    } catch (error) {
      console.error('Erro ao trocar organização:', error);
      // Mesmo em caso de erro, atualizar a organização selecionada
      setCurrentOrganization(org);
      localStorage.setItem('currentOrganizationId', org.id);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        currentOrganization,
        userOrganizations,
        member,
        isLoading,
        login,
        register,
        logout,
        setCurrentOrganization: handleSetCurrentOrganization,
        refreshUserOrganizations,
      }}
    >
            {children}   {' '}
    </AuthContext.Provider>
  );
};
