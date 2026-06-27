import Login from "./components/Login";
import RequestList from "./components/RequestList";
import { useState } from "react";
import Register from "./components/Register";
import './App.css';
import Toast from "./components/Toast";

/**
 * Hlavní komponenta aplikace.
 * Řídí přihlášení, registraci a zobrazení toast notifikací.
 */
function App() {

  // Stav přihlášení uživatele
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  // Přepínač mezi přihlášením a registrací
  const [showRegister, setShowRegister] = useState(false)

  // Aktuálně zobrazená toast notifikace
  const [toast, setToast] = useState(null);

  // Zobrazí toast notifikaci s textem a typem (success/error)
  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const [isLoading, setIsLoading] = useState(false)


  return (
    <div>
      {
        toast && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast(null)}
          />
        )
      }
      {isLoggedIn ? (
        <RequestList showToast={showToast} />
      ) : showRegister ? (
        <Register onShowLogin={() => setShowRegister(false)}
          onSuccess={() => setShowRegister(false)}
          showToast={showToast}
        />
      ) : (
        <Login
          onLoginSuccess={setIsLoggedIn}
          onShowRegister={() => setShowRegister(true)}
          showToast={showToast}
        />
      )}
    </div>
  );
}

export default App;