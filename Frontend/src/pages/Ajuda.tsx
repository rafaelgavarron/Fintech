import { useState, useRef, useEffect } from "react";

// Define a estrutura de uma mensagem no chat
interface ChatMessage {
    id: number;
    text: string;
    sender: 'user' | 'bot';
}

function Ajuda() {
    const [isChatBotOpen, setIsChatBotOpen] = useState(false);
    const [inputMessage, setInputMessage] = useState("");
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const messagesEndRef = useRef<HTMLDivElement>(null);

    // Lista de Dúvidas Frequentes (Comandos do Bot)
    const faqData = [
        {
            question: "Como registro uma nova transação?",
            answer: "No Dashboard ou na página de Transações, utilize os botões 'Adicionar Receita' ou 'Adicionar Despesa'. Preencha o valor, a data e a descrição. Lembre-se de ter uma Conta Bancária cadastrada em Configurações.",
        },
        {
            question: "Por que meu Saldo está zerado?",
            answer: "O saldo é o resultado da soma de todas as Receitas menos Despesas. Verifique se você já cadastrou contas e lançou suas transações corretamente. Se o problema persistir, pode ser uma falha na conexão com a API.",
        },
        {
            question: "Como funciona a funcionalidade de Metas?",
            answer: "Você pode criar uma meta definindo um 'Valor Alvo' e uma 'Data Final'. O sistema acompanha seu progresso. Para começar, acesse a aba 'Metas' e clique em 'Adicionar nova Meta'.",
        },
        {
            question: "Posso alterar o nome da minha Organização?",
            answer: "Sim. Vá para 'Configurações' > 'Configurações da Organização'. Você poderá editar o nome da organização e salvar as alterações.",
        },
    ];

    // Mapeamento para buscar a resposta mais facilmente
    const faqMap = new Map(faqData.map(item => [item.question.toLowerCase(), item.answer]));

    // Efeito para rolar o chat para a última mensagem
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    // Inicializa o chat com a mensagem de boas-vindas do bot e as opções
    const initializeChat = () => {
        const initialMessage: ChatMessage = {
            id: 1,
            text: "Olá! Sou seu assistente FinPath. Posso responder a estas dúvidas rápidas:",
            sender: 'bot',
        };
        setMessages([initialMessage]);
    };

    // Abre e fecha o chatbot e inicializa as mensagens
    const toggleChatBot = () => {
        if (!isChatBotOpen) {
            initializeChat();
        }
        setIsChatBotOpen(!isChatBotOpen);
    };

    // Lógica de envio da mensagem do usuário e resposta do bot
    const handleSendMessage = (text: string) => {
        if (!text.trim()) return;

        // 1. Adiciona a mensagem do usuário
        const newUserMessage: ChatMessage = {
            id: Date.now(),
            text: text,
            sender: 'user',
        };
        setMessages((prevMessages) => [...prevMessages, newUserMessage]);
        
        setInputMessage(""); // Limpa o input

        // 2. Lógica de resposta do Bot
        setTimeout(() => {
            let botResponseText = "Desculpe, não entendi sua pergunta. Por favor, tente um dos comandos disponíveis ou reformule sua dúvida.";
            
            const normalizedText = text.toLowerCase().trim();

            // Tenta encontrar uma resposta exata ou parcial
            let matchedAnswer = faqMap.get(normalizedText);

            if (!matchedAnswer) {
                // Tenta encontrar uma correspondência parcial (muito simplificado)
                for (const [key, value] of faqMap.entries()) {
                    if (normalizedText.includes(key.split('?')[0].toLowerCase())) {
                        matchedAnswer = value;
                        break;
                    }
                }
            }
            
            if (matchedAnswer) {
                botResponseText = matchedAnswer;
            } else if (normalizedText.includes('olá') || normalizedText.includes('oi')) {
                botResponseText = "Olá! Como posso te ajudar hoje com suas finanças?";
            }
            
            // 3. Adiciona a resposta do Bot
            const newBotMessage: ChatMessage = {
                id: Date.now() + 1,
                text: botResponseText,
                sender: 'bot',
            };
            setMessages((prevMessages) => [...prevMessages, newBotMessage]);
        }, 500); // Simula um tempo de resposta
    };

    // Função de clique nos botões de comando
    const handleCommandClick = (question: string) => {
        handleSendMessage(question);
    };

    // Submissão do formulário
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        handleSendMessage(inputMessage);
    };


    return (
        <div className="main-content">
            <div className="container-fluid">
                <h1 className="h3 fw-bold text-light mb-1">Central de Ajuda</h1>
                <p className="text-muted mb-5">
                    Encontre as respostas para as dúvidas mais comuns ou inicie um chat com nosso assistente.
                </p>

                {/* Seção de Dúvidas Frequentes (Acordeão) */}
                <h3 className="h4 fw-bold text-light mb-3 mt-4">Dúvidas Frequentes (FAQ)</h3>
                
                <div className="accordion" id="faqAccordion">
                    {faqData.map((item, index) => (
                        <div className="accordion-item" key={`faq-${index}`} style={{ 
                            backgroundColor: 'var(--color-secondary-dark)', 
                            border: '1px solid #292D41',
                            marginBottom: '10px',
                            borderRadius: '8px'
                        }}>
                            <h2 className="accordion-header" id={`heading${index}`}>
                                <button
                                    className="accordion-button collapsed"
                                    type="button"
                                    data-bs-toggle="collapse"
                                    data-bs-target={`#collapse${index}`}
                                    aria-expanded="false"
                                    aria-controls={`collapse${index}`}
                                    style={{ 
                                        backgroundColor: 'var(--color-secondary-dark)', 
                                        color: 'var(--color-text-light)',
                                        fontWeight: '600',
                                        borderRadius: '8px'
                                    }}
                                >
                                    {item.question}
                                </button>
                            </h2>
                            <div
                                id={`collapse${index}`}
                                className="accordion-collapse collapse"
                                aria-labelledby={`heading${index}`}
                                data-bs-parent="#faqAccordion"
                            >
                                <div className="accordion-body text-muted" style={{ 
                                    backgroundColor: 'var(--color-primary-dark)', 
                                    borderTop: '1px solid #292D41',
                                    borderRadius: '0 0 8px 8px'
                                }}>
                                    {item.answer}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Botão Flutuante para Abrir o Chat Bot */}
                <button
                    className="btn btn-primary rounded-circle shadow-lg"
                    onClick={toggleChatBot}
                    style={{
                        position: 'fixed',
                        bottom: '30px',
                        right: '30px',
                        width: '60px',
                        height: '60px',
                        fontSize: '24px',
                        zIndex: 1000,
                        padding: 0
                    }}
                    title="Abrir Chat Bot"
                >
                    <i className="bi bi-chat-dots-fill"></i>
                </button>

                {/* Chat Bot Overlay */}
                {isChatBotOpen && (
                    <div 
                        className="chat-bot-overlay"
                        style={{
                            position: 'fixed',
                            top: 0,
                            left: 0,
                            right: 0,
                            bottom: 0,
                            backgroundColor: 'rgba(0, 0, 0, 0.5)', // Fundo semi-transparente
                            zIndex: 999,
                            display: 'flex',
                            justifyContent: 'flex-end',
                            alignItems: 'flex-end',
                        }}
                        onClick={toggleChatBot}
                    >
                        <div 
                            className="chat-bot-container card shadow-2xl p-0"
                            style={{
                                width: '350px',
                                height: '450px',
                                margin: '30px',
                                borderRadius: '12px',
                                display: 'flex',
                                flexDirection: 'column',
                                backgroundColor: 'var(--color-secondary-dark)',
                                border: '1px solid var(--color-brand-accent)'
                            }}
                            onClick={(e) => e.stopPropagation()}
                        >
                            {/* Cabeçalho do Chat */}
                            <div className="card-header d-flex justify-content-between align-items-center" style={{
                                backgroundColor: 'var(--color-brand-accent)',
                                color: 'white',
                                padding: '15px',
                                borderTopLeftRadius: '12px',
                                borderTopRightRadius: '12px',
                                borderBottom: '1px solid #5B45E1'
                            }}>
                                <h5 className="mb-0 text-white fw-bold">Assistente FinPath</h5>
                                <button className="btn btn-sm text-white" onClick={toggleChatBot}>
                                    <i className="bi bi-x-lg"></i>
                                </button>
                            </div>
                            
                            {/* Corpo do Chat (Mensagens) */}
                            <div className="card-body flex-grow-1" style={{ overflowY: 'auto', padding: '15px', display: 'flex', flexDirection: 'column' }}>
                                
                                {messages.map((msg) => (
                                    <div 
                                        key={msg.id} 
                                        className={`d-flex mb-2 ${msg.sender === 'user' ? 'justify-content-end' : 'justify-content-start'}`}
                                    >
                                        <div 
                                            className={`p-2 rounded-3 text-break shadow-sm`}
                                            style={{
                                                maxWidth: '80%',
                                                fontSize: '0.9rem',
                                                backgroundColor: msg.sender === 'user' ? 'var(--color-brand-accent)' : 'var(--color-primary-dark)',
                                                color: msg.sender === 'user' ? 'white' : 'var(--color-text-light)',
                                                border: msg.sender === 'bot' ? '1px solid #292D41' : 'none'
                                            }}
                                        >
                                            {msg.text}
                                        </div>
                                    </div>
                                ))}

                                {/* Comandos Rápidos do Bot (mostrado após a primeira mensagem do bot) */}
                                {messages.length === 1 && messages[0].sender === 'bot' && (
                                    <div className="d-flex flex-wrap gap-2 mt-2">
                                        {faqData.map((item, index) => (
                                            <button
                                                key={`cmd-${index}`}
                                                className="btn btn-sm btn-outline-info"
                                                onClick={() => handleCommandClick(item.question)}
                                                style={{ fontSize: '0.75rem' }}
                                            >
                                                {item.question.split('?')[0]}
                                            </button>
                                        ))}
                                    </div>
                                )}

                                <div ref={messagesEndRef} />
                            </div>

                            {/* Área de Input */}
                            <div className="card-footer" style={{ 
                                padding: '10px 15px', 
                                borderTop: '1px solid #292D41',
                                backgroundColor: 'var(--color-primary-dark)',
                                borderBottomLeftRadius: '12px',
                                borderBottomRightRadius: '12px'
                            }}>
                                <form onSubmit={handleSubmit} className="d-flex">
                                    <input 
                                        type="text" 
                                        className="form-control me-2" 
                                        placeholder="Digite sua pergunta..."
                                        value={inputMessage}
                                        onChange={(e) => setInputMessage(e.target.value)}
                                        style={{ 
                                            backgroundColor: 'var(--color-secondary-dark)',
                                            border: '1px solid #4B5563',
                                            color: 'var(--color-text-light)'
                                        }}
                                    />
                                    <button type="submit" className="btn btn-primary" title="Enviar">
                                        <i className="bi bi-send-fill"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Ajuda;