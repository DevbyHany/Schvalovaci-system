import { useState } from "react";
import API_BASE_URL from '../config';


function Register({ onShowLogin, onSuccess, showToast }) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessages, setErrorMessages] = useState([]);

    const handleRegister = async () => {
        const response = await fetch(`${API_BASE_URL}/api/requests`, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({ name, email, password })
        });

        const data = await response.json();


        if (response.ok) {
            showToast("Registrace proběhla úspěšně! Můžeš se přihlásit.")
            onSuccess();
        } else {
            const errors = data.errors || data.message;
            if (Array.isArray(errors)) {
                setErrorMessages(errors);
            } else if (typeof errors === 'string') {
                setErrorMessages(errors.split(', '));
            }
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Registrace</h2>
                <input
                    type="text"
                    placeholder="Zadej své jméno"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="login-input"
                />
                <input
                    type="email"
                    placeholder="Zadej svůj email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="login-input"
                />
                <input
                    type="password"
                    placeholder="Zadej své heslo"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="login-input"
                />
                <button className="login-button" onClick={handleRegister}>Registrovat se</button>
                {errorMessages.length > 0 && (
                    <div className="error-message">
                        <ul style={{ margin: 0, paddingLeft: '0', listStyle: 'none' }}>
                            {errorMessages.map((err, index) => (
                                <li key={index}>{err}</li>
                            ))}
                        </ul>
                    </div>
                )}
                <p className="login-link">
                    Už máš účet? <span onClick={onShowLogin}>Přihlas se</span>
                </p>
            </div>
        </div>
    )
}

export default Register;