import { useState, useRef } from "react";
import API_BASE_URL from '../config';

function CreateRequest({ onSuccess, onClose, currentUser, showToast }) {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [errorMessages, setErrorMessages] = useState([]);
    const mouseDownOnOverlay = useRef (false);

    const handleSubmit = async () => {
        console.log(title, description);
        const credentials = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/api/requests`, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + credentials,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ title, description })
        })
        if (response.ok) {
            showToast('Žádost byla úspěšně vytvořena');
            onSuccess();
        } else {
            const errorData = await response.json();
            const errors = errorData.message || 'Vytvoření žádosti selhalo';
            if (typeof errors === 'string') {
                setErrorMessages(errors.split(', '));
            }
        }
    }

    const handleOverlayMouseDown = (e) => {
        mouseDownOnOverlay.current = e.target === e.currentTarget;
    };

    const handleOverlayMouseUp = (e) => {
        if (mouseDownOnOverlay.current && e.target === e.currentTarget) {
            onClose();
        }
        mouseDownOnOverlay.current = false;
    };

    return (
        <div
            className="modal-overlay"
            onMouseDown={handleOverlayMouseDown}
            onMouseUp={handleOverlayMouseUp}
        >
            <div className="modal-card" onClick={(e) => e.stopPropagation()}>

                <div
                    className="modal-top">
                    <h2>Vytvoření nové žádosti</h2>
                    <button className="modal-close" onClick={onClose}>×</button>
                </div>

                <div className="modal-body">
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Název žádosti"
                        className="inputTitle"
                    />
                    <textarea
                        className="inputDescription"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Podrobnější popis žádosti"
                    />
                    {errorMessages.length > 0 && (
                        <div className="error-message">
                            {errorMessages.map((err, index) => (
                                <p key={index} style={{ margin: '4px 0' }}>{err}</p>
                            ))}
                        </div>
                    )}
                    <button className="createButton" onClick={handleSubmit}>Vytvořit žádost</button>
                </div>

            </div>
        </div>
    )
}

export default CreateRequest;