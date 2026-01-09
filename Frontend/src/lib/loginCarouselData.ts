// lib/loginCarouselData.ts

export interface CarouselItem {
  image: string; 
  title: string;
  description: string;
}

export const loginCarouselItems: CarouselItem[] = [
  {
    image: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80',
    title: 'Entenda seus Gastos',
    description: 'Visualize todas as suas finanças em um lugar. O controle total na palma da sua mão.',
  },
  {
    image: 'https://images.unsplash.com/photo-1639322537228-f710d846310a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1632&q=80',
    title: 'Invista com Inteligência',
    description: 'Aprenda com nossos cursos e faça seu dinheiro trabalhar por você, sem complicações.',
  },
  {
    image: 'https://img.freepik.com/fotos-gratis/motivacao-de-metas-de-solucao-de-proposta-de-trabalho-excelente_53876-167120.jpg?',
    title: 'Alcance Suas Metas',
    description: 'Da sua reserva de emergência à viagem dos sonhos. Nós te ajudamos a chegar lá.',
  },
];