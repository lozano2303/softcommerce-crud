document.addEventListener('DOMContentLoaded', function() {
    // URLs de la API
    const apiUrl = 'http://localhost:8080/api/v1/paymentorder/';
    const apiPayments = 'http://localhost:8080/api/v1/payment/';
    const apiOrders = 'http://localhost:8080/api/v1/order/';

    // Variables globales
    let allPaymentOrders = [];
    let allPayments = [];
    let allOrders = [];

    // Función para mostrar relaciones en la tabla
function displayPaymentOrders(paymentOrders) {
    const tableBody = document.getElementById('paymentOrderTableBody');
    tableBody.innerHTML = '';

    paymentOrders.forEach(po => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>Pago #${po.id.paymentID}</td>
            <td>Orden #${po.id.orderID}</td>
            <td>${po.payment.method || 'N/A'}</td>
            <td>${po.payment.status ? '<span class="badge bg-success">Activo</span>' : '<span class="badge bg-danger">Inactivo</span>'}</td>
            <td>${po.order.status ? '<span class="badge bg-success">Activa</span>' : '<span class="badge bg-danger">Inactiva</span>'}</td>
            <td class="action-column">
                <button class="btn btn-sm btn-danger btn-delete" 
                        data-payment-id="${po.id.paymentID}" 
                        data-order-id="${po.id.orderID}" 
                        title="Eliminar relación">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tableBody.appendChild(row);
    });

    addDeleteButtonListeners();
}

// Función para poblar selects
function populateSelect(selectId, items, valueField, textField) {
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="">Seleccione...</option>';
    
    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item[valueField];
        // Mostrar "Pago #X" o "Orden #Y" según corresponda
        option.textContent = selectId === 'selectPayment' 
            ? `Pago #${item[valueField]}` 
            : `Orden #${item[valueField]}`;
        select.appendChild(option);
    });
}

    // Cargar todas las relaciones
    async function loadPaymentOrders() {
        try {
            showLoading(true);
            const response = await fetch(apiUrl);
            if (!response.ok) throw new Error('Error al cargar relaciones');
            
            allPaymentOrders = await response.json();
            displayPaymentOrders(allPaymentOrders);
        } catch (error) {
            showAlert('Error al cargar relaciones: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Cargar pagos y órdenes para los selects
async function loadPaymentsAndOrders() {
    try {
        showLoading(true);
        
        // Cargar pagos activos
        const paymentsResponse = await fetch(apiPayments + '?status=true');
        if (!paymentsResponse.ok) throw new Error('Error al cargar pagos');
        allPayments = await paymentsResponse.json();
        
        // Cargar órdenes no pagadas
        const ordersResponse = await fetch(apiOrders + '?paid=false');
        if (!ordersResponse.ok) throw new Error('Error al cargar órdenes');
        allOrders = await ordersResponse.json();
        
        // Poblar selects con el formato solicitado
        populateSelect('selectPayment', allPayments, 'paymentID');
        populateSelect('selectOrder', allOrders, 'orderID');
        
    } catch (error) {
        showAlert('Error al cargar datos: ' + error.message, 'danger');
    } finally {
        showLoading(false);
    }
}

    // Función para poblar selects
    function populateSelect(selectId, items, valueField) {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Seleccione...</option>';
        
        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueField];
            option.textContent = selectId === 'selectPayment' 
                ? `Pago #${item[valueField]}` 
                : `Orden #${item[valueField]}`;
            select.appendChild(option);
        });
    
        if (items.length === 0) {
            showAlert(`No hay datos disponibles para ${selectId === 'selectPayment' ? 'Pagos' : 'Órdenes'}`, 'info');
        }
    }

    // Filtrar relaciones
    function filterPaymentOrders() {
        const paymentId = document.getElementById('paymentFilter').value;
        const orderId = document.getElementById('orderFilter').value;
        
        let filtered = allPaymentOrders;
        
        if (paymentId) {
            filtered = filtered.filter(po => po.id.paymentID == paymentId);
        }
        
        if (orderId) {
            filtered = filtered.filter(po => po.id.orderID == orderId);
        }
        
        displayPaymentOrders(filtered);
    }

    // Asignar pago a orden
    document.getElementById('addPaymentOrderForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const paymentId = document.getElementById('selectPayment').value;
        const orderId = document.getElementById('selectOrder').value;
        
        if (!paymentId || !orderId) {
            showAlert('Debe seleccionar un pago y una orden válidos', 'warning');
            return;
        }
    
        try {
            showLoading(true);
            
            const requestBody = {
                paymentID: parseInt(paymentId),
                orderID: parseInt(orderId)
            };
    
            console.log("Datos a enviar:", requestBody); // Debug en consola del navegador
    
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });
    
            if (!response.ok) {
                const errorData = await response.json();
                console.error("Error response:", errorData); // Debug del error
                throw new Error(errorData.message || 'Error al asignar pago');
            }
    
            const responseData = await response.json();
            showAlert(responseData.message || 'Pago asignado correctamente a la orden', 'success');
            document.getElementById('addPaymentOrderForm').reset();
            await loadPaymentOrders();
            await loadPaymentsAndOrders();
            bootstrap.Modal.getInstance(document.getElementById('addPaymentOrderModal')).hide();
        } catch (error) {
            console.error('Error detallado:', error); // Debug más detallado
            showAlert('Error al asignar pago: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    });

    // Actualizar estado de una orden
    async function updateOrderStatus(orderId, status) {
        try {
            const response = await fetch(`${apiOrders}${orderId}/status`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ status: status })
            });
            
            if (!response.ok) {
                throw new Error('Error al actualizar estado de la orden');
            }
        } catch (error) {
            console.error('Error al actualizar orden:', error);
        }
    }

    // Eliminar relación
    async function deletePaymentOrder(paymentId, orderId) {
        if (!confirm('¿Está seguro de eliminar esta relación?')) return;
        
        try {
            showLoading(true);
            const response = await fetch(`${apiUrl}${paymentId}/${orderId}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Error al eliminar relación');
            }
            
            showAlert('Relación eliminada correctamente', 'success');
            await loadPaymentOrders();
        } catch (error) {
            showAlert('Error al eliminar relación: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Agregar event listeners a los botones de eliminar
    function addDeleteButtonListeners() {
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function() {
                const paymentId = this.getAttribute('data-payment-id');
                const orderId = this.getAttribute('data-order-id');
                deletePaymentOrder(paymentId, orderId);
            });
        });
    }

    // Mostrar/ocultar loading
    function showLoading(show) {
        document.getElementById('loadingIndicator').style.display = show ? 'block' : 'none';
    }

    // Mostrar alertas
    function showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show mt-3`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        
        const container = document.querySelector('.container-fluid > .row > .col-md-9');
        if (container) {
            const oldAlerts = container.querySelectorAll('.alert');
            oldAlerts.forEach(alert => alert.remove());
            
            container.prepend(alertDiv);
            setTimeout(() => alertDiv.remove(), 5000);
        }
    }

    // Event listeners para filtros
    document.getElementById('paymentFilter').addEventListener('input', filterPaymentOrders);
    document.getElementById('orderFilter').addEventListener('input', filterPaymentOrders);

    // Inicialización
    loadPaymentOrders();
    loadPaymentsAndOrders();
});