import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './login/Login';
import Register from './register/Register';
import Chat from './chat/Chat';


function App() {
    return (
        <Router>
            <Routes>
                {}
                <Route path="/login" element={<Login />} />

                <Route path="/chat" element={<Chat />} />

                <Route path="/register" element=
                {<Register />} />
                <Route path="/" element={<Navigate to="/login" replace />} />

                {}
                <Route path="*" element={<h1 style={{color: 'white', textAlign: 'center'}}>404 - Sidan hittades inte</h1>} />
            </Routes>
        </Router>
    );
}

export default App;