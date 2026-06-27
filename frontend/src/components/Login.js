import {useState} from "react";
import API_BASE_URL from '../config';

function Login({onLoginSuccess, onShowRegister}) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false)
    const handleLogin = async () => {
        if (!email.trim()) {
            setErrorMessage('Email nesmí být prázdný');
            return;
        }
        if (!password.trim()) {
            setErrorMessage('Heslo nesmí být prázdné');
            return;
        }
        setIsLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/api/requests`, {
                headers: {
                    'Authorization': 'Basic ' + btoa(email + ':' + password)
                }
            });
            if (response.ok) {
                localStorage.setItem('credentials', btoa(email + ':' + password))
                onLoginSuccess(true);
            } else {
                if (response.status === 401) {
                    setErrorMessage('Špatné přihlašovací údaje');
                } else {
                    const errorData = await response.json();
                    setErrorMessage(errorData.message || 'Chyba při přihlášení')
                }
            }
        } catch (error) {
            setErrorMessage("Nepodařilo se připojit se serverem");
        } finally {
            setIsLoading(false);
        }
    }


    return (<div className="login-container">
        <div className="login-card">
            <h2>Přihlášení</h2>
            <div>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => {
                        setEmail(e.target.value);
                        setErrorMessage('');
                    }}
                    onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
                    className="login-input"
                />
                <input
                    type="password"
                    placeholder="Heslo"
                    value={password}
                    onChange={(e) => {
                        setPassword(e.target.value);
                        setErrorMessage('');
                    }}
                    onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
                    className="login-input"
                />
            </div>
            <button className="login-button" onClick={handleLogin} disabled={isLoading}>
                <span className="login-button-content">
                    {isLoading && <span className="login-spinner"></span>}
                    {isLoading ? 'Přihlašuji…' : 'Přihlásit se'}
                </span>
            </button>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <p className="login-link">
                Nemáš účet? <span onClick={onShowRegister}>Zaregistruj se</span>
            </p>
        </div>
    </div>);
}


export default Login;