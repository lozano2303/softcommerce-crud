document.addEventListener('DOMContentLoaded', function() {
    // URLs de la API
    const apiUrlPayments = 'http://localhost:8080/api/v1/payment/';
    const apiUrlUsers = 'http://localhost:8080/api/v1/user/';

    // Variables globales para almacenar datos
    let allPayments = [];
    let allUsers = [];

    // Función para mostrar pagos en la tabla
    function displayPayments(payments) {
        const tableBody = document.getElementById('paymentsTableBody');
        if (!tableBody) {
            console.error('No se encontró el elemento paymentsTableBody');
            return;
        }
    
        tableBody.innerHTML = '';
    
        if (!Array.isArray(payments)) {
            console.error('Los datos recibidos no son un array:', payments);
            showAlert('Error: Formato de datos incorrecto', 'danger');
            return;
        }
    
        payments.forEach(payment => {
            try {
                const row = document.createElement('tr');
                
                row.innerHTML = `
                    <td>${payment.paymentID || 'N/A'}</td>
                    <td>${payment.user?.name || payment.userID || 'N/A'}</td>
                    <td>$${(payment.amount || 0).toFixed(2)}</td>
                    <td>${payment.method || 'N/A'}</td>
                    <td>${payment.status ? 'Activo' : 'Inactivo'}</td>
                    <td>${payment.createdAt ? new Date(payment.createdAt).toLocaleString() : 'N/A'}</td>
                    <td class="action-column">
                        <button class="btn btn-sm btn-primary me-1 btn-edit"
                                data-payment-id="${payment.paymentID}" title="Editar">
                            <i class="fa fa-edit"></i>
                        </button>
                        ${payment.status
                            ? `<button class="btn btn-sm btn-danger me-1 btn-delete"
                                    data-payment-id="${payment.paymentID}" title="Desactivar">
                                <i class="fa fa-trash"></i>
                              </button>`
                            : `<button class="btn btn-sm btn-success me-1 btn-reactivate"
                                    data-payment-id="${payment.paymentID}" title="Reactivar">
                                <i class="fa fa-arrow-up"></i>
                              </button>`
                        }
                    </td>
                `;
                tableBody.appendChild(row);
            } catch (error) {
                console.error('Error al crear fila para:', payment, error);
            }
        });
    
        // Agregar event listeners a los botones
        addEditButtonListeners();
        addDeleteButtonListeners();
        addReactivateButtonListeners();
    }

    // Función para cargar los pagos
    async function loadPayments() {
        try {
            showLoading(true);
            const response = await fetch(apiUrlPayments);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();
            console.log('Pagos recibidos:', data);
            
            allPayments = data;
            displayPayments(data);
        } catch (error) {
            console.error('Error al cargar pagos:', error);
            showAlert('Error al cargar pagos: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Función para cargar usuarios en el select
    async function loadUsersForSelect() {
        try {
            showLoading(true);
            const response = await fetch(apiUrlUsers);
            if (!response.ok) throw new Error('Error al cargar usuarios');
            
            allUsers = await response.json();
            
            // Poblar select de usuarios
            populateSelect('userID', allUsers, 'userID', 'name');
            populateSelect('editUserID', allUsers, 'userID', 'name');
            
        } catch (error) {
            console.error('Error cargando usuarios:', error);
            showAlert('Error cargando usuarios: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Función para filtrar pagos
    function filterPayments() {
        const method = document.getElementById('methodFilter').value.toLowerCase();
        const status = document.getElementById('statusFilter').value;

        let filteredPayments = allPayments;

        if (method) {
            filteredPayments = filteredPayments.filter(p => 
                p.method.toLowerCase().includes(method)
            );
        }

        if (status) {
            const statusBool = status === 'true';
            filteredPayments = filteredPayments.filter(p => p.status === statusBool);
        }

        displayPayments(filteredPayments);
    }

    // Función para poblar selects
    function populateSelect(selectId, items, valueField, textField) {
        const select = document.getElementById(selectId);
        if (!select) {
            console.error(`Select con ID ${selectId} no encontrado`);
            return;
        }

        select.innerHTML = '<option value="">Seleccione...</option>';
        
        if (!Array.isArray(items)) {
            console.error('Los items no son un array:', items);
            return;
        }

        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueField];
            option.textContent = item[textField];
            select.appendChild(option);
        });
    }

    // Función para mostrar el modal de edición
    function showEditModal(paymentId) {
        const payment = allPayments.find(p => p.paymentID == paymentId);
        if (!payment) {
            showAlert('No se encontró el pago', 'danger');
            return;
        }

        // Llenar el formulario de edición
        document.getElementById('editPaymentId').value = payment.paymentID;
        document.getElementById('editUserID').value = payment.user?.userID || payment.userID;
        document.getElementById('editAmount').value = payment.amount;
        document.getElementById('editMethod').value = payment.method;
        document.getElementById('editStatus').checked = payment.status;
        
        // Mostrar el modal
        const editModal = new bootstrap.Modal(document.getElementById('editPaymentModal'));
        editModal.show();
    }

    // Función para agregar event listeners a los botones de editar
    function addEditButtonListeners() {
        document.querySelectorAll('.btn-edit').forEach(button => {
            button.addEventListener('click', function() {
                const paymentId = this.getAttribute('data-payment-id');
                showEditModal(paymentId);
            });
        });
    }

    // Función para agregar event listeners a los botones de desactivar
    function addDeleteButtonListeners() {
        document.querySelectorAll('.btn-delete').forEach(button => {
            button.addEventListener('click', function() {
                const paymentId = this.getAttribute('data-payment-id');
                disablePayment(paymentId);
            });
        });
    }

    // Función para agregar event listeners a los botones de reactivar
    function addReactivateButtonListeners() {
        document.querySelectorAll('.btn-reactivate').forEach(button => {
            button.addEventListener('click', function() {
                const paymentId = this.getAttribute('data-payment-id');
                reactivatePayment(paymentId);
            });
        });
    }

    // Función para mostrar/ocultar carga
    function showLoading(show) {
        const loader = document.getElementById('loadingIndicator');
        if (loader) loader.style.display = show ? 'block' : 'none';
    }

    // Función para mostrar alertas
    function showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show mt-3`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        
        const container = document.querySelector('.container-fluid > .row > .col-md-9');
        if (container) {
            // Eliminar alertas anteriores
            const oldAlerts = container.querySelectorAll('.alert');
            oldAlerts.forEach(alert => alert.remove());
            
            container.prepend(alertDiv);
            
            // Auto-eliminar después de 5 segundos
            setTimeout(() => alertDiv.remove(), 5000);
        }
    }

    // Función para desactivar pago
    async function disablePayment(paymentId) {
        if (!confirm('¿Está seguro de desactivar este pago?')) {
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(`${apiUrlPayments}disable/${paymentId}`, {
                method: 'PUT'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al desactivar pago');
            }

            showAlert('Pago desactivado correctamente', 'success');
            await loadPayments();
        } catch (error) {
            console.error('Error al desactivar pago:', error);
            showAlert(error.message || 'Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Función para reactivar pago
    async function reactivatePayment(paymentId) {
        if (!confirm('¿Está seguro de reactivar este pago?')) {
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(`${apiUrlPayments}reactivate/${paymentId}`, {
                method: 'PUT'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al reactivar pago');
            }

            showAlert('Pago reactivado correctamente', 'success');
            await loadPayments();
        } catch (error) {
            console.error('Error al reactivar pago:', error);
            showAlert(error.message || 'Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Resto del código (registrar nuevo pago, actualizar pago, etc.) permanece igual
    document.getElementById('addPaymentForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const userID = document.getElementById('userID').value;
        const amount = document.getElementById('amount').value;
        const method = document.getElementById('method').value;

        if (!userID || !amount || !method) {
            showAlert('Todos los campos son requeridos', 'warning');
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(apiUrlPayments, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userID: userID,
                    amount: amount,
                    method: method
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al registrar pago');
            }

            const result = await response.json();
            
            showAlert('Pago registrado correctamente', 'success');
            document.getElementById('addPaymentForm').reset();
            await loadPayments();
            bootstrap.Modal.getInstance(document.getElementById('addPaymentModal')).hide();
        } catch (error) {
            console.error('Error al registrar pago:', error);
            showAlert(error.message || 'Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
        }
    });

    document.getElementById('editPaymentForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const paymentId = document.getElementById('editPaymentId').value;
        const userID = document.getElementById('editUserID').value;
        const amount = document.getElementById('editAmount').value;
        const method = document.getElementById('editMethod').value;
        const status = document.getElementById('editStatus').checked;

        if (!userID || !amount || !method) {
            showAlert('Todos los campos son requeridos', 'warning');
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(`${apiUrlPayments}${paymentId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userID: userID,
                    amount: amount,
                    method: method,
                    status: status
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al actualizar pago');
            }

            showAlert('Pago actualizado correctamente', 'success');
            await loadPayments();
            bootstrap.Modal.getInstance(document.getElementById('editPaymentModal')).hide();
        } catch (error) {
            console.error('Error al actualizar pago:', error);
            showAlert(error.message || 'Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
        }
    });

    // Event listeners para los filtros
    document.getElementById('methodFilter').addEventListener('input', filterPayments);
    document.getElementById('statusFilter').addEventListener('change', filterPayments);

    // Inicialización
    loadPayments();
    loadUsersForSelect();
});