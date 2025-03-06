function showModal(title, message, type = 'info') {
    const modal = document.getElementById('resultModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalBody = document.getElementById('modalBody');

    modalTitle.textContent = title;
    modalBody.textContent = message;

    const headerElement = modal.querySelector('.modal-header');
    headerElement.className = 'modal-header';
    switch(type) {
        case 'success':
            headerElement.classList.add('bg-success', 'text-white');
            break;
        case 'error':
            headerElement.classList.add('bg-danger', 'text-white');
            break;
        case 'warning':
            headerElement.classList.add('bg-warning');
            break;
        default:
            headerElement.classList.add('bg-info', 'text-white');
    }

    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
}
