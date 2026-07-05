import {translateStatus, formatDate} from "../utils";
import {useState, useRef} from "react";
import API_BASE_URL from '../config';


function RequestDetail({request, onClose, currentUser, showToast}) {

    const [errorMessage, setErrorMessage] = useState('');

    const [showCancelModal, setShowCancelModal] = useState(false);

    const [cancellationReason, setCancellationReason] = useState('');

    const mouseDownOnOverlay = useRef(false);

    const handleReject = async () => {
        const credentials = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/api/requests/${request.id}/reject`, {
            method: 'PUT', headers: {
                'Authorization': 'Bearer ' + credentials, 'Content-Type': 'application/json'
            },
        })
        if (response.ok) {
            showToast('Žádost byla zamítnuta', 'error');
            onClose();
        } else {
            const errorData = await response.json();
            setErrorMessage(errorData.message || 'Žádost již byla zamítnuta');
        }
    }

    const handleCancel = async () => {
        const credentials = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/api/requests/${request.id}/cancel`, {
            method: 'PUT', body: JSON.stringify({cancellationReason: cancellationReason}), headers: {
                'Authorization': 'Bearer ' + credentials, 'Content-Type': 'application/json'
            }
        })
        if (response.ok) {
            showToast('Žádost byla zrušena', 'error');
            setShowCancelModal(false);
            onClose();
        } else {
            const errorDat = await response.json();
            setErrorMessage(errorDat.message || 'Žádost již byla zrušena');
        }
    }

    const canCancel = request.requestStatus === 'PENDING' && (request.creator.id === currentUser?.id || currentUser?.role === 'ADMIN');

    const handleApprove = async () => {
        const credentials = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/api/requests/${request.id}/approve`, {
            method: 'PUT', headers: {
                'Authorization': 'Bearer ' + credentials, 'Content-Type': 'application/json'
            },
        })
        if (response.ok) {
            showToast('Žádost byla schválena')
            onClose();
        } else {
            const errorData = await response.json();
            setErrorMessage(errorData.message || 'Žádost již byla schválena');
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

    return (<div
            className="modal-overlay"
            onMouseDown={handleOverlayMouseDown}
            onMouseUp={handleOverlayMouseUp}
        >
            <div className="modal-card">
                {!showCancelModal ? (<>
                        <div className="modal-top">
                            <h2>Detail žádosti</h2>
                            <button className="modal-close" onClick={onClose}>×</button>
                        </div>

                        <div className="modal-body">
                            <div className="detail-row">
                                <span className="detail-label">Název</span>
                                <span className="detail-value">{request.title}</span>
                            </div>

                            <div className="detail-row">
                                <span className="detail-label">Popis</span>
                                <span className="detail-value">{request.description}</span>
                            </div>

                            <div className="detail-row">
                                <span className="detail-label">Status</span>
                                <div>
                                    <span className={`status status-${request.requestStatus.toLowerCase()}`}>
                                        {translateStatus(request.requestStatus)}
                                    </span>
                                </div>
                            </div>

                            <div className="detail-row">
                                <span className="detail-label">Vytvořeno</span>
                                <span className="detail-value">{formatDate(request.createdAt)}</span>
                            </div>

                            <div className="detail-row">
                                <span className="detail-label">Žadatel</span>
                                <span className="detail-value">{request.creator.name}</span>
                            </div>

                            {request.requestStatus === 'CANCELLED' && (<>
                                    <div className="detail-row">
                                        <span className="detail-label">Zrušil</span>
                                        <span className="detail-value">{request.canceler?.name}</span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="detail-label">Důvod zrušení</span>
                                        <span className="detail-value">{request.cancellationReason}</span>
                                    </div>
                                </>)}

                        </div>

                        {errorMessage && <p className="error-message">{errorMessage}</p>}

                        {(currentUser?.role === 'APPROVER' || currentUser?.role === 'ADMIN') && request.requestStatus === 'PENDING' && request.creator.id !== currentUser.id && (
                            <div className="modal-footer">
                                <button className="detailApproveButton" onClick={handleApprove}>Schválit</button>
                                <button className="detailRejectButton" onClick={handleReject}>Zamítnout</button>
                            </div>)}

                        {canCancel && (<div className="modal-footer">
                                <button
                                    className="detailCancelButton"
                                    onClick={() => setShowCancelModal(true)}
                                >
                                    Zrušit žádost
                                </button>
                            </div>)}
                    </>) : (<>
                        <div className="modal-top">
                            <h2>Zrušení žádosti</h2>
                            <button className="modal-close" onClick={() => setShowCancelModal(false)}>×</button>
                        </div>

                        <div className="modal-body">
                            <p>Opravdu chceš zrušit tuto žádost? Uveď prosím důvod:</p>
                            <textarea
                                className="inputDescription"
                                value={cancellationReason}
                                onChange={(e) => setCancellationReason(e.target.value)}
                                placeholder="Důvod zrušení…"
                            />
                        </div>

                        {errorMessage && <p className="error-message">{errorMessage}</p>}

                        <div className="modal-footer">
                            <button
                                className="detailRejectButtonBack"
                                onClick={() => setShowCancelModal(false)}
                            >
                                Zpět
                            </button>
                            <span
                                className="cancel-tooltip-wrap"
                                data-tooltip={!cancellationReason.trim() ? 'Nejprve vyplň důvod zrušení' : ''}
                            >
                                <button
                                    className="detailCancelButton"
                                    onClick={handleCancel}
                                    disabled={!cancellationReason.trim()}
                                >
                                    Potvrdit zrušení
                                </button>
                            </span>
                        </div>
                    </>)}
            </div>
        </div>);
}

export default RequestDetail;