import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../login/Login.css';

export default function Register() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: '',
        displayName: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [id]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');


        if (formData.password !== formData.confirmPassword) {
            setError("Lösenorden matchar inte!");
            return;
        }

        try {

            const response = await fetch('/api/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: formData.username,
                    displayName: formData.displayName,
                    password: formData.password
                }),
            });

            if (response.ok) {
                console.log("Registrering lyckades!");
                navigate('/login');
            } else {
                const contentType = response.headers.get('content-type') || '';
                let message = "Registreringen misslyckades.";

                if (contentType.includes('application/json')) {
                    const data = await response.json();
                    message = data?.message || message;
                } else {
                    const text = (await response.text()).trim();
                    if (text) message = text;
                }

                setError(message);
            }
        } catch (err) {
            setError("Kunde inte ansluta till servern. Kontrollera din minikube tunnel!");
            console.error("Fel vid registrering:", err);
        }

    };

    return (
        <div className="form-wrapper">
            <div className="form-container">
                <h1 style={{ fontSize: '1.8rem', textAlign: 'center', marginBottom: '20px' }}>
                    Registrera dig
                </h1>

                {error && <div className="error-message" style={{ color: 'red', marginBottom: '15px', textAlign: 'center' }}>{error}</div>}

                <form className="register-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Användarnamn</label>
                        <input type="text" id="username" value={formData.username} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="displayName">Visningsnamn (Display Name)</label>
                        <input type="text" id="displayName" value={formData.displayName} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Lösenord</label>
                        <input type="password" id="password" value={formData.password} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Bekräfta lösenord</label>
                        <input type="password" id="confirmPassword" value={formData.confirmPassword} onChange={handleChange} required />
                    </div>

                    <button type="submit" className="btn-primary">Skapa konto</button>
                </form>

                <div style={{ marginTop: '20px', textAlign: 'center', fontSize: '0.9rem' }}>
                    <p style={{ marginBottom: '8px' }}>
                        Har du redan ett konto? <Link to="/login" style={{ color: 'var(--color-accent)' }}>Logga in</Link>
                    </p>
                    <p>
                        <Link to="/login" style={{ color: 'var(--color-muted)' }}>Tillbaka</Link>
                    </p>
                </div>
            </div>
        </div>
    );
}