import { useEffect } from "react";
import { useState } from "react";
import CreateRequest from "./CreateRequest";
import RequestDetail from "./RequestDetail";
import { translateStatus, formatDate } from "../utils";
import UserMenu from "./UserMenu";


function RequestList({ showToast }) {
    const [requests, setRequests] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const [selectedRequest, setSelectedRequest] = useState(null)
    const [filter, setFilter] = useState('ALL')
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        handleRequestList();
        fetchCurrentUser();
    }, []);
    const filteredRequests = requests.filter(r => {
        if (filter === 'ALL') return true;
        return r.requestStatus === filter;
    });
    const fetchCurrentUser = async () => {
        const credentials = localStorage.getItem('credentials');
        const response = await fetch('http://localhost:8080/api/users/me', {
            headers: {
                'Authorization': 'Basic ' + credentials
            }
        });
        const data = await response.json();
        console.log(data);
        setCurrentUser(data);
    }
    const handleRequestList = async () => {
        const credentials = localStorage.getItem('credentials');
        const response = await fetch('http://localhost:8080/api/requests', {
            headers: {
                'Authorization': 'Basic ' + (credentials)
            }
        });
        const data = await response.json();
        console.log(data)
        setRequests(data);
        setLoading(false);
    }
    return (
        <div className="page-wrapper">
            <div className="header">
                <div className="header-title">
                    <div className="header-icon">✓</div>
                    <h2>Schvalovací systém</h2>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                    {currentUser?.role !== 'ADMIN' && (
                        <button className="createButton" onClick={() => setShowForm(true)}>
                            + Nová žádost
                        </button>
                    )}
                    <UserMenu
                        currentUser={currentUser}
                        onLogout={() => {
                            localStorage.removeItem('credentials');
                            window.location.reload();
                        }}
                    />
                </div>
            </div>

            <div className="content-wrapper">
                <div className="filter-bar">
                    <button className={`filter-btn ${filter === 'ALL' ? 'active' : ''}`} onClick={() => setFilter('ALL')}>
                        Všechny <span className="filter-count">{requests.length}</span>
                    </button>
                    <button className={`filter-btn pending ${filter === 'PENDING' ? 'active' : ''}`} onClick={() => setFilter('PENDING')}>
                        Čeká se <span className="filter-count">{requests.filter(r => r.requestStatus === 'PENDING').length}</span>
                    </button>
                    <button className={`filter-btn approved ${filter === 'APPROVED' ? 'active' : ''}`} onClick={() => setFilter('APPROVED')}>
                        Schváleno <span className="filter-count">{requests.filter(r => r.requestStatus === 'APPROVED').length}</span>
                    </button>
                    <button className={`filter-btn rejected ${filter === 'REJECTED' ? 'active' : ''}`} onClick={() => setFilter('REJECTED')}>
                        Zamítnuto <span className="filter-count">{requests.filter(r => r.requestStatus === 'REJECTED').length}</span>
                    </button>
                </div>
                <div className="table-card">
                    {loading ? (
                        <div className="table-loading">
                            <div className="spinner"></div>
                            <p>Načítám žádosti...</p>
                        </div>
                    ) : filteredRequests.length === 0 ? (
                        <div className="table-empty">
                            <p>📭</p>
                            <p>
                                {filter === 'ALL'
                                    ? 'Zatím zde nejsou žádné žádosti'
                                    : `Žádné žádosti ve stavu "${translateStatus(filter)}"`}
                            </p>
                        </div>
                    ) : (
                        <table>
                            <thead>
                                <tr>
                                    <th>Název</th>
                                    <th>Status</th>
                                    <th>Vytvořeno</th>
                                    <th>Žadatel</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredRequests.map(request => (
                                    <tr key={request.id} onClick={() => setSelectedRequest(request)}>
                                        <td>{request.title}</td>
                                        <td>
                                            <span className={`status status-${request.requestStatus.toLowerCase()}`}>
                                                {translateStatus(request.requestStatus)}
                                            </span>
                                        </td>
                                        <td>{formatDate(request.createdAt)}</td>
                                        <td>{request.creator.name}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
            {showForm && (
                <CreateRequest
                    onSuccess={() => { setShowForm(false); handleRequestList(); }}
                    onClose={() => setShowForm(false)}
                    currentUser={currentUser}
                    showToast={showToast}
                />
            )}
            {selectedRequest && (
                <RequestDetail
                    request={selectedRequest}
                    onClose={() => { setSelectedRequest(null); handleRequestList(); }}
                    currentUser={currentUser}
                    showToast={showToast}
                />
            )}
        </div>
    );
}


export default RequestList;