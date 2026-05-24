export const translateStatus = (status) => {
    if (status === 'PENDING') return 'Čeká se';
    if (status === 'APPROVED') return 'Schváleno';
    if (status === 'REJECTED') return 'Zamítnuto';
    return status;
};

export const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('cs-CZ', {
        day: 'numeric',
        month: 'numeric',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
};