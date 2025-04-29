document.addEventListener('DOMContentLoaded', function() {
    // URLs de la API
    const apiUrlOrderProducts = 'http://localhost:8080/api/v1/orderproduct/';
    const apiUrlProducts = 'http://localhost:8080/api/v1/products/';
    const apiUrlOrders = 'http://localhost:8080/api/v1/order/';

    // Variables para almacenar datos
    let allOrderProducts = [];
    let allProducts = [];
    let allOrders = [];

    // Función para mostrar productos pedidos en la tabla
    function displayOrderProducts(orderProducts) {
        const tableBody = document.getElementById('productOrdersTableBody');
        if (!tableBody) {
            console.error('No se encontró el elemento productOrdersTableBody');
            return;
        }

        tableBody.innerHTML = ''; // Limpiar la tabla

        if (!Array.isArray(orderProducts)) {
            console.error('Los datos recibidos no son un array:', orderProducts);
            showAlert('Error: Formato de datos incorrecto', 'danger');
            return;
        }

        orderProducts.forEach(item => {
            try {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.product?.name || item.productName || 'N/A'}</td>
                    <td>Pedido N° ${item.order?.orderID || item.orderID || 'N/A'}</td>
                    <td>${item.quantity || 0}</td>
                    <td>$${(item.subtotal || 0).toFixed(2)}</td>
                    <td>
                        <button class="btn btn-sm btn-primary me-1 edit-btn" 
                                data-order-id="${item.order?.orderID || item.orderID}"
                                data-product-id="${item.product?.productID || item.productID}"
                                data-quantity="${item.quantity}">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger me-1 delete-btn" 
                                data-order-id="${item.order?.orderID || item.orderID}"
                                data-product-id="${item.product?.productID || item.productID}">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            } catch (error) {
                console.error('Error al crear fila para:', item, error);
            }
        });

        // Agregar event listeners a los botones
        addEditButtonListeners();
        addDeleteButtonListeners();
    }

    // Función para cargar los productos pedidos
    async function loadOrderProducts() {
        try {
            showLoading(true);
            const response = await fetch(apiUrlOrderProducts);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();
            console.log('Datos recibidos:', data);
            
            allOrderProducts = data;
            displayOrderProducts(data);
        } catch (error) {
            console.error('Error al cargar productos pedidos:', error);
            showAlert('Error al cargar productos pedidos: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Función para cargar productos y pedidos en los selects
    async function loadProductsAndOrdersForMenus() {
        try {
            showLoading(true);
            
            // Cargar productos
            const productsResponse = await fetch(apiUrlProducts);
            if (!productsResponse.ok) throw new Error('Error al cargar productos');
            allProducts = await productsResponse.json();
            
            // Cargar pedidos
            const ordersResponse = await fetch(apiUrlOrders);
            if (!ordersResponse.ok) throw new Error('Error al cargar pedidos');
            allOrders = await ordersResponse.json();
            
            // Poblar selects
            populateSelect('productFilter', allProducts, 'productID', 'name');
            populateSelect('productName', allProducts, 'productID', 'name');
            populateSelect('orderFilter', allOrders, 'orderID', 'orderID', 'Pedido N° ');
            populateSelect('orderID', allOrders, 'orderID', 'orderID', 'Pedido N° ');
            
        } catch (error) {
            console.error('Error cargando datos:', error);
            showAlert('Error cargando datos: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Función para filtrar los productos pedidos
    function filterOrderProducts() {
        const selectedProductId = document.getElementById('productFilter').value;
        const selectedOrderId = document.getElementById('orderFilter').value;

        let filteredProducts = allOrderProducts;

        if (selectedProductId) {
            filteredProducts = filteredProducts.filter(item => 
                (item.product?.productID || item.productID) == selectedProductId
            );
        }

        if (selectedOrderId) {
            filteredProducts = filteredProducts.filter(item => 
                (item.order?.orderID || item.orderID) == selectedOrderId
            );
        }

        displayOrderProducts(filteredProducts);
    }

    // Función para poblar selects
    function populateSelect(selectId, items, valueField, textField, prefix = '') {
        const select = document.getElementById(selectId);
        if (!select) {
            console.error(`Select con ID ${selectId} no encontrado`);
            return;
        }

        select.innerHTML = '<option value="">Todos</option>';
        
        if (!Array.isArray(items)) {
            console.error('Los items no son un array:', items);
            return;
        }

        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueField];
            option.textContent = prefix + item[textField];
            select.appendChild(option);
        });
    }

    // Función para mostrar el modal de edición
    function showEditModal(orderId, productId, currentQuantity) {
        // Buscar el producto pedido completo
        const orderProduct = allOrderProducts.find(op => 
            (op.order?.orderID || op.orderID) == orderId && 
            (op.product?.productID || op.productID) == productId
        );

        if (!orderProduct) {
            showAlert('No se encontró el producto pedido', 'danger');
            return;
        }

        // Llenar el formulario de edición
        document.getElementById('editOrderId').value = orderId;
        document.getElementById('editProductId').value = productId;
        document.getElementById('editQuantity').value = currentQuantity;
        
        // Mostrar información adicional
        document.getElementById('editProductName').textContent = orderProduct.product?.name || orderProduct.productName || 'N/A';
        document.getElementById('editOrderNumber').textContent = `Pedido N° ${orderId}`;
        
        // Mostrar el modal
        const editModal = new bootstrap.Modal(document.getElementById('editProductOrderModal'));
        editModal.show();
    }

    // Función para agregar event listeners a los botones de editar
    function addEditButtonListeners() {
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', function() {
                const orderId = this.getAttribute('data-order-id');
                const productId = this.getAttribute('data-product-id');
                const quantity = this.getAttribute('data-quantity');
                showEditModal(orderId, productId, quantity);
            });
        });
    }

    // Función para agregar event listeners a los botones de eliminar
    function addDeleteButtonListeners() {
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function() {
                const orderId = this.getAttribute('data-order-id');
                const productId = this.getAttribute('data-product-id');
                deleteOrderProduct(orderId, productId);
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

    // Función para agregar nuevo producto pedido
    document.getElementById('addProductOrderForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const productId = document.getElementById('productName').value;
        const orderId = document.getElementById('orderID').value;
        const quantity = document.getElementById('quantity').value;

        if (!productId || !orderId || !quantity) {
            showAlert('Todos los campos son requeridos', 'warning');
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(apiUrlOrderProducts, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    orderID: orderId,
                    productID: productId,
                    quantity: quantity
                })
            });

            const result = await response.json();
            
            if (result.status === 'success') {
                showAlert('Producto agregado al pedido correctamente', 'success');
                document.getElementById('addProductOrderForm').reset();
                await loadOrderProducts(); // Recargar la tabla
                filterOrderProducts(); // Aplicar filtros actuales
            } else {
                showAlert(result.message || 'Error al agregar producto al pedido', 'danger');
            }
        } catch (error) {
            console.error('Error al agregar producto pedido:', error);
            showAlert('Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
            bootstrap.Modal.getInstance(document.getElementById('addProductOrderModal')).hide();
        }
    });

    // Función para mostrar el modal de edición con los datos actuales
function showEditModal(orderId, productId) {
    // Buscar el producto pedido en los datos cargados
    const orderProduct = allOrderProducts.find(op => 
        (op.order?.orderID || op.orderID) == orderId && 
        (op.product?.productID || op.productID) == productId
    );

    if (!orderProduct) {
        showAlert('No se encontró el producto pedido', 'danger');
        return;
    }

    // Llenar el formulario de edición
    document.getElementById('editOrderId').value = orderId;
    document.getElementById('editProductId').value = productId;
    document.getElementById('editQuantity').value = orderProduct.quantity;
    
    // Mostrar información de referencia
    document.getElementById('editProductName').textContent = orderProduct.product?.name || 'N/A';
    document.getElementById('editOrderNumber').textContent = `Pedido N° ${orderId}`;
    
    // Mostrar el modal
    const editModal = new bootstrap.Modal(document.getElementById('editProductOrderModal'));
    editModal.show();
}

// Función para manejar el envío del formulario de edición
document.getElementById('editProductOrderForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const orderId = document.getElementById('editOrderId').value;
    const productId = document.getElementById('editProductId').value;
    const quantity = document.getElementById('editQuantity').value;

    if (!quantity || quantity <= 0) {
        showAlert('La cantidad debe ser mayor que cero', 'warning');
        return;
    }

    try {
        showLoading(true);
        
        // Preparar los datos para enviar (según tu DTO)
        const updateData = {
            orderID: parseInt(orderId),
            productID: parseInt(productId),
            quantity: parseInt(quantity),
            subtotal: 0 // El backend lo recalculará
        };

        // Hacer la llamada PUT al endpoint correcto
        const response = await fetch(`${apiUrlOrderProducts}${orderId}/${productId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        const result = await response.json();
        
        if (result.status === 'success') {
            showAlert('Producto actualizado correctamente', 'success');
            await loadOrderProducts(); // Recargar los datos
            filterOrderProducts(); // Reaplicar filtros
            bootstrap.Modal.getInstance(document.getElementById('editProductOrderModal')).hide();
        } else {
            showAlert(result.message || 'Error al actualizar el producto', 'danger');
        }
    } catch (error) {
        console.error('Error al actualizar:', error);
        showAlert('Error al conectar con el servidor', 'danger');
    } finally {
        showLoading(false);
    }
});

    // Función para eliminar un producto pedido
    async function deleteOrderProduct(orderId, productId) {
        if (!confirm('¿Está seguro de eliminar este producto del pedido?')) {
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(`${apiUrlOrderProducts}${orderId}/${productId}`, {
                method: 'DELETE'
            });

            const result = await response.json();
            
            if (result.status === 'success') {
                showAlert('Producto eliminado del pedido', 'success');
                await loadOrderProducts(); // Recargar la tabla
                filterOrderProducts(); // Aplicar filtros actuales
            } else {
                showAlert(result.message || 'Error al eliminar producto', 'danger');
            }
        } catch (error) {
            console.error('Error al eliminar producto:', error);
            showAlert('Error al conectar con el servidor', 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Event listeners para los filtros
    document.getElementById('productFilter').addEventListener('change', filterOrderProducts);
    document.getElementById('orderFilter').addEventListener('change', filterOrderProducts);

    // Inicialización
    loadOrderProducts();
    loadProductsAndOrdersForMenus();
});