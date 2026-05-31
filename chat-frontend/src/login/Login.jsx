import React, { useState } from 'react';
import '../../Login.css';
import {useNavigate} from "react-router-dom";

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        try {

            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (response.ok) {
                const data = await response.json();


                if (data.token) {
                    localStorage.setItem('token', data.token);
                }

                console.log("🎉 Inloggning lyckades!");

                navigate('/chat');
            } else {

                setErrorMessage('Fel användarnamn eller lösenord');
            }

        } catch (error) {
            console.error("Nätverksfel:", error);
            setErrorMessage('Kunde inte ansluta till servern. Är din BFF igång?');
        }
    };

    return (
        <div className="form-wrapper">
            <div className="form-container">
                <h2>Logga in</h2>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Användarnamn</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            autoFocus
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Lösenord</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button className="btn-primary" type="submit">Logga in</button>
                </form>

                <p style={{ marginTop: '15px', textAlign: 'center', fontSize: '0.9rem' }}>
                    Har du inget konto? <a href="/register" style={{ color: 'var(--color-accent)' }}>Registrera</a>
                </p>
            </div>
        </div>
    );
}