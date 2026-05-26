import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../login/Login.css';


export default function Register() {

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: ''
    });


    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [id]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();


        if (formData.password !== formData.confirmPassword) {
            alert("Lösenorden matchar inte!");
            return;
        }

        console.log("Skickar registreringsdata till BFF:", formData);
    };

    return (
        <div className="form-wrapper">
            <div className="form-container">
                <h1 style={{ fontSize: '1.8rem', textAlign: 'center', marginBottom: '20px' }}>
                    Registrera dig
                </h1>

                <form className="register-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="firstName">Förnamn</label>
                        <input type="text" id="firstName" value={formData.firstName} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="lastName">Efternamn</label>
                        <input type="text" id="lastName" value={formData.lastName} onChange={handleChange} required />
                    </div>


                    <div className="form-group">
                        <label htmlFor="email">E-post</label>
                        <input type="email" id="email" value={formData.email} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="phone">Mobilnummer</label>
                        <input type="tel" id="phone" value={formData.phone} onChange={handleChange} />
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