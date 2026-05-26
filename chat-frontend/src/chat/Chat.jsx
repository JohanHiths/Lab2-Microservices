import React, { useState } from 'react';
import './chat.css';

export default function Chat() {

    const [messages, setMessages] = useState([
        { id: 1, text: "Välkommen till chatten! Välj en bot-personlighet ovan.", isUser: false },
        { id: 2, text: "Testmeddelande från användaren", isUser: true }
    ]);
    const [inputMessage, setInputMessage] = useState('');
    const [personality, setPersonality] = useState('coder');

    const handleSendMessage = (e) => {
        e.preventDefault();
        if (!inputMessage.trim()) return;


        const newMsg = {
            id: Date.now(),
            text: inputMessage,
            isUser: true
        };
        setMessages(prev => [...prev, newMsg]);
        setInputMessage('');


        console.log(`Skickar till Kafka via BFF (${personality}):`, inputMessage);
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