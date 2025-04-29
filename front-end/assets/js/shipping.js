document.addEventListener('DOMContentLoaded', function() {
    // URLs de la API
    const apiUrl = 'http://localhost:8080/api/v1/shipping/';
    const apiOrders = 'http://localhost:8080/api/v1/order/';

    // Variables globales
    let allShippings = [];
    let allOrders = [];

    // Función para mostrar envíos en la tabla
    function displayShippings(shippings) {
        const tableBody = document.getElementById('shippingTableBody');
        tableBody.innerHTML = '';

        shippings.forEach(shipping => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${shipping.shippingID}</td>
                <td>Orden #${shipping.order ? shipping.order.orderID : 'N/A'}</td>
                <td>${shipping.address}</td>
                <td>${shipping.city}</td>
                <td>${shipping.country}</td>
                <td>${shipping.postal_code}</td>
                <td>${shipping.status ? '<span class="badge bg-success">Activo</span>' : '<span class="badge bg-danger">Inactivo</span>'}</td>
                <td>${new Date(shipping.created_at).toLocaleString()}</td>
                <td class="action-column">
                    <button class="btn btn-sm btn-primary btn-edit" 
                            data-shipping-id="${shipping.shippingID}" 
                            title="Editar envío">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger btn-delete" 
                            data-shipping-id="${shipping.shippingID}" 
                            title="Eliminar envío">
                        <i class="fa fa-trash"></i>
                    </button>
                    <button class="btn btn-sm ${shipping.status ? 'btn-warning' : 'btn-success'} btn-toggle-status" 
                            data-shipping-id="${shipping.shippingID}" 
                            data-current-status="${shipping.status}"
                            title="${shipping.status ? 'Desactivar' : 'Activar'}">
                        <i class="fa ${shipping.status ? 'fa-times' : 'fa-check'}"></i>
                    </button>
                </td>
            `;
            tableBody.appendChild(row);
        });

        addButtonListeners();
    }

    // Actualización de addButtonListeners
function addButtonListeners() {
    // Botones de editar
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', function() {
            const shippingId = this.getAttribute('data-shipping-id');
            loadShippingForEdit(shippingId);
        });
    });
    
    // Botones de eliminar/desactivar
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', function() {
            const shippingId = this.getAttribute('data-shipping-id');
            deleteShipping(shippingId);
        });
    });
    
    // Botones de reactivar
    document.querySelectorAll('.btn-reactivate').forEach(btn => {
        btn.addEventListener('click', function() {
            const shippingId = this.getAttribute('data-shipping-id');
            reactivateShipping(shippingId);
        });
    });
}

    // Función para poblar select de órdenes
    function populateOrderSelect(selectId, orders) {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Seleccione una orden...</option>';
        
        orders.forEach(order => {
            const option = document.createElement('option');
            option.value = order.orderID;
            option.textContent = `Orden #${order.orderID}`;
            select.appendChild(option);
        });
    }

    // Cargar todos los envíos
    async function loadShippings() {
        try {
            showLoading(true);
            const response = await fetch(apiUrl);
            if (!response.ok) throw new Error('Error al cargar envíos');
            
            allShippings = await response.json();
            displayShippings(allShippings);
        } catch (error) {
            showAlert('Error al cargar envíos: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Cargar órdenes para el select
    async function loadOrders() {
        try {
            showLoading(true);
            const response = await fetch(apiOrders);
            if (!response.ok) throw new Error('Error al cargar órdenes');
            
            allOrders = await response.json();
            populateOrderSelect('selectOrder', allOrders);
            populateOrderSelect('editSelectOrder', allOrders);
        } catch (error) {
            showAlert('Error al cargar órdenes: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Filtrar envíos
    function filterShippings() {
        const orderId = document.getElementById('orderFilter').value;
        const address = document.getElementById('addressFilter').value;
        const city = document.getElementById('cityFilter').value;
        const country = document.getElementById('countryFilter').value;
        const postalCode = document.getElementById('postalCodeFilter').value;
        
        let filtered = allShippings;
        
        if (orderId) {
            filtered = filtered.filter(s => 
                s.order && s.order.orderID == orderId
            );
        }
        
        if (address) {
            filtered = filtered.filter(s => 
                s.address.toLowerCase().includes(address.toLowerCase())
            );
        }
        
        if (city) {
            filtered = filtered.filter(s => 
                s.city.toLowerCase().includes(city.toLowerCase())
            );
        }
        
        if (country) {
            filtered = filtered.filter(s => 
                s.country.toLowerCase().includes(country.toLowerCase())
            );
        }
        
        if (postalCode) {
            filtered = filtered.filter(s => 
                s.postal_code.toLowerCase().includes(postalCode.toLowerCase())
            );
        }
        
        displayShippings(filtered);
    }

    // Crear nuevo envío
    document.getElementById('addShippingForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const orderId = document.getElementById('selectOrder').value;
        const address = document.getElementById('address').value;
        const city = document.getElementById('city').value;
        const country = document.getElementById('country').value;
        const postalCode = document.getElementById('postalCode').value;
        
        // Validación básica del frontend
        if (!orderId || !address || !city || !country || !postalCode) {
            showAlert('Todos los campos son requeridos', 'warning');
            return;
        }
    
        try {
            showLoading(true);
            
            const requestBody = {
                orderID: parseInt(orderId),
                address: address,
                city: city,
                country: country,
                postal_code: postalCode,
                created_at: new Date().toISOString()
            };

            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });

            const responseData = await response.json();
    
            if (!response.ok) {
                throw new Error(responseData.message || 'Error al crear envío');
            }
    
            showAlert('Envío creado exitosamente', 'success');
            document.getElementById('addShippingForm').reset();
            await loadShippings();
            bootstrap.Modal.getInstance(document.getElementById('addShippingModal')).hide();
        } catch (error) {
            showAlert('Error al crear envío: ' + error.message, 'danger');
            console.error('Error detallado:', error);
        } finally {
            showLoading(false);
        }
    });

    // Editar envío (cargar datos en modal)
    async function loadShippingForEdit(shippingId) {
        console.log("Cargando envío con ID:", shippingId);
        try {
            showLoading(true);
            const response = await fetch(`${apiUrl}${shippingId}`);
            
            if (!response.ok) {
                throw new Error('Error al cargar el envío');
            }
            
            const shipping = await response.json();
            console.log("Datos completos del envío:", shipping);
    
            // Verifica si shippingID existe, de lo contrario usa el parámetro shippingId
            const idToUse = shipping.shippingID || shippingId;
            
            // Asignar valores a los campos del modal
            document.getElementById('editShippingId').value = idToUse;
            document.getElementById('editSelectOrder').value = shipping.orderID;
            document.getElementById('editAddress').value = shipping.address;
            document.getElementById('editCity').value = shipping.city;
            document.getElementById('editCountry').value = shipping.country;
            document.getElementById('editPostalCode').value = shipping.postal_code;
    
            const editModal = new bootstrap.Modal(document.getElementById('editShippingModal'));
            editModal.show();
            
        } catch (error) {
            showAlert('Error al cargar envío: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }
    
    
    
    
    
    // Actualizar envío
    document.getElementById('editShippingForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const shippingId = document.getElementById('editShippingId').value;  // Verifica que aquí se obtenga un valor válido
        const orderId = document.getElementById('editSelectOrder').value;
        const address = document.getElementById('editAddress').value;
        const city = document.getElementById('editCity').value;
        const country = document.getElementById('editCountry').value;
        const postalCode = document.getElementById('editPostalCode').value;
        
        // Verifica que shippingId tenga un valor antes de hacer la solicitud
        if (!shippingId || shippingId === 'undefined') {
            showAlert('El ID del envío es inválido', 'danger');
            return;
        }
    
        try {
            showLoading(true);
            
            const requestBody = {
                orderID: parseInt(orderId),
                address: address,
                city: city,
                country: country,
                postal_code: postalCode,
                created_at: new Date().toISOString()
            };
    
            const response = await fetch(`${apiUrl}${shippingId}`, {
                method: 'PUT',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });
    
            const responseData = await response.json();
        
            if (!response.ok) {
                throw new Error(responseData.message || 'Error al actualizar envío');
            }
        
            showAlert('Envío actualizado exitosamente', 'success');
            await loadShippings();
            bootstrap.Modal.getInstance(document.getElementById('editShippingModal')).hide();
        } catch (error) {
            showAlert('Error al actualizar envío: ' + error.message, 'danger');
            console.error('Error detallado:', error);
        } finally {
            showLoading(false);
        }
    });
    

    // Función para desactivar envío (eliminación lógica)
async function deleteShipping(shippingId) {
    if (!confirm('¿Está seguro de desactivar este envío?')) return;
    
    try {
        showLoading(true);
        const response = await fetch(`${apiUrl}deactivate/${shippingId}`, {
            method: 'PUT'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al desactivar envío');
        }
        
        const result = await response.json();
        showAlert(result.message, 'success');
        await loadShippings();
    } catch (error) {
        showAlert('Error al desactivar envío: ' + error.message, 'danger');
    } finally {
        showLoading(false);
    }
}

// Función para reactivar envío
async function reactivateShipping(shippingId) {
    if (!confirm('¿Está seguro de reactivar este envío?')) return;
    
    try {
        showLoading(true);
        const response = await fetch(`${apiUrl}reactivate/${shippingId}`, {
            method: 'PUT'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al reactivar envío');
        }
        
        const result = await response.json();
        showAlert(result.message, 'success');
        await loadShippings();
    } catch (error) {
        showAlert('Error al reactivar envío: ' + error.message, 'danger');
    } finally {
        showLoading(false);
    }
}

    // Cambiar estado del envío
    async function toggleShippingStatus(shippingId, currentStatus) {
        try {
            showLoading(true);
            
            // Usar el endpoint correcto según el estado actual
            const endpoint = currentStatus ? 'deactivate' : 'reactivate';
            const response = await fetch(`${apiUrl}${endpoint}/${shippingId}`, {
                method: 'PUT',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });
            
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || `Error al ${currentStatus ? 'desactivar' : 'reactivar'} envío`);
            }
            
            const result = await response.json();
            showAlert(result.message, 'success');
            await loadShippings();
        } catch (error) {
            showAlert(error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Agregar event listeners a los botones
    function addButtonListeners() {
        // Botones de editar
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', function() {
                const shippingId = this.getAttribute('data-shipping-id');
                loadShippingForEdit(shippingId);
            });
        });
        
        // Botones de eliminar
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function() {
                const shippingId = this.getAttribute('data-shipping-id');
                deleteShipping(shippingId);
            });
        });
        
        // Botones de cambiar estado
        document.querySelectorAll('.btn-toggle-status').forEach(btn => {
            btn.addEventListener('click', function() {
                const shippingId = this.getAttribute('data-shipping-id');
                const currentStatus = this.getAttribute('data-current-status') === 'true';
                toggleShippingStatus(shippingId, currentStatus);
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
    document.getElementById('orderFilter').addEventListener('input', filterShippings);
    document.getElementById('addressFilter').addEventListener('input', filterShippings);
    document.getElementById('cityFilter').addEventListener('input', filterShippings);
    document.getElementById('countryFilter').addEventListener('input', filterShippings);
    document.getElementById('postalCodeFilter').addEventListener('input', filterShippings);

    // Inicialización
    loadShippings();
    loadOrders();
});