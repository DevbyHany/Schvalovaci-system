import Login from "./components/Login";
import RequestList from "./components/RequestList";
import { useState } from "react";
import Register from "./components/Register";
import './App.css';
import Toast from "./components/Toast";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [showRegister, setShowRegister] = useState(false)
  const [toast, setToast] = useState(null);
  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };


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