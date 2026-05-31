import React, { useState } from 'react';
import './chat.css';

export default function Chat() {

    const [messages, setMessages] = useState([
        { id: 1, text: "Välkommen till chatten! Välj en bot-personlighet ovan.", isUser: false },
        { id: 2, text: "Testmeddelande från användaren", isUser: true }
    ]);
    const [inputMessage, setInputMessage] = useState('');
    const [personality, setPersonality] = useState('coder');

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputMessage.trim()) return;

        const messageData = {
            content: inputMessage,
            senderId: "1",
            personality: personality
        };

        try {

            const response = await fetch('/api/messages', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(messageData)
            });

            if (response.ok) {
                setMessages(prev => [...prev, { id: Date.now(), text: inputMessage, isUser: true }]);
                setInputMessage('');
            } else {
                console.error("Kunde inte skicka meddelande:", response.statusText);
            }
        } catch (error) {
            console.error("Nätverksfel vid sändning:", error);
        }
    };

    return (
        <div className="form-wrapper">
            <div className="container chat-container">
                <h2>🤖 AI Chat</h2>

                <div className="select-row">
                    <select value={personality} onChange={(e) => setPersonality(e.target.value)}>
                        <option value="coder">💻 Coder </option>
                        <option value="pirate">🏴‍☠️ Pirate </option>
                        <option value="robot">🤖 Robot</option>
                        <option value="philosopher">🤔 Philosopher</option>
                    </select>
                </div>

                {}
                <div className="chat-box">
                    {messages.map((msg) => (
                        <div key={msg.id} className={`bubble ${msg.isUser ? 'user' : 'ai'}`}>
                            {msg.text}
                        </div>
                    ))}
                </div>

                {}
                <form onSubmit={handleSendMessage} className="input-row">
                    <input
                        type="text"
                        id="message"
                        placeholder="Skriv ett meddelande..."
                        value={inputMessage}
                        onChange={(e) => setInputMessage(e.target.value)}
                        required
                        autoComplete="off"
                    />
                    <button type="submit">Skicka</button>
                </form>
            </div>
        </div>
    );
}