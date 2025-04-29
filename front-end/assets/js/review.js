document.addEventListener('DOMContentLoaded', function() {
    // URLs de la API
    const apiUrl = 'http://localhost:8080/api/v1/review/';
    const apiUsers = 'http://localhost:8080/api/v1/user/';
    const apiProducts = 'http://localhost:8080/api/v1/products/';

    // Variables globales
    let allReviews = [];
    let allUsers = [];
    let allProducts = [];

    // Función para mostrar reseñas en la tabla
    function displayReviews(reviews) {
        const tableBody = document.getElementById('reviewTableBody');
        tableBody.innerHTML = '';

        reviews.forEach(review => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${review.reviewID}</td>
                <td>${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</td>
                <td>${review.comment}</td>
                <td>${review.user ? 'Usuario #' + review.user.userID : 'N/A'}</td>
                <td>${review.product ? 'Producto #' + review.product.productID : 'N/A'}</td>
                <td>${new Date(review.createdAt).toLocaleString()}</td>
                <td class="action-column">
                    <button class="btn btn-sm btn-primary btn-edit" 
                            data-review-id="${review.reviewID}" 
                            title="Editar reseña">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger btn-delete" 
                            data-review-id="${review.reviewID}" 
                            title="Eliminar reseña">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            `;
            tableBody.appendChild(row);
        });

        addButtonListeners();
    }

    // Función para poblar selects
    function populateSelect(selectId, items, valueField, textField) {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Seleccione...</option>';
        
        items.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueField];
            option.textContent = textField === 'user' 
                ? `Usuario #${item[valueField]} - ${item.name || 'Sin nombre'}`
                : `Producto #${item[valueField]} - ${item.name || 'Sin nombre'}`;
            select.appendChild(option);
        });
    }

    // Cargar todas las reseñas
    async function loadReviews() {
        try {
            showLoading(true);
            const response = await fetch(apiUrl);
            if (!response.ok) throw new Error('Error al cargar reseñas');
            
            allReviews = await response.json();
            displayReviews(allReviews);
        } catch (error) {
            showAlert('Error al cargar reseñas: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Cargar usuarios y productos para los selects
    async function loadUsersAndProducts() {
        try {
            showLoading(true);
            
            // Cargar usuarios
            const usersResponse = await fetch(apiUsers);
            if (!usersResponse.ok) throw new Error('Error al cargar usuarios');
            allUsers = await usersResponse.json();
            
            // Cargar productos
            const productsResponse = await fetch(apiProducts);
            if (!productsResponse.ok) throw new Error('Error al cargar productos');
            allProducts = await productsResponse.json();
            
            // Poblar selects
            populateSelect('selectUser', allUsers, 'userID', 'user');
            populateSelect('selectProduct', allProducts, 'productID', 'product');
            
        } catch (error) {
            showAlert('Error al cargar datos: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Filtrar reseñas
    function filterReviews() {
        const rating = document.getElementById('ratingFilter').value;
        const comment = document.getElementById('commentFilter').value;
        const userId = document.getElementById('userFilter').value;
        const productId = document.getElementById('productFilter').value;
        
        let filtered = allReviews;
        
        if (rating) {
            filtered = filtered.filter(r => r.rating == rating);
        }
        
        if (comment) {
            filtered = filtered.filter(r => 
                r.comment.toLowerCase().includes(comment.toLowerCase())
            );
        }
        
        if (userId) {
            filtered = filtered.filter(r => 
                r.user && r.user.userID == userId
            );
        }
        
        if (productId) {
            filtered = filtered.filter(r => 
                r.product && r.product.productID == productId
            );
        }
        
        displayReviews(filtered);
    }

    // Crear nueva reseña
    document.getElementById('addReviewForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const rating = document.getElementById('rating').value;
        const comment = document.getElementById('comment').value;
        const userId = document.getElementById('selectUser').value;
        const productId = document.getElementById('selectProduct').value;
        
        // Validación básica del frontend
        if (!rating || !comment || !userId || !productId) {
            showAlert('Todos los campos son requeridos', 'warning');
            return;
        }
    
        try {
            showLoading(true);
            
            const requestBody = {
                rating: parseInt(rating),
                comment: comment,
                userID: parseInt(userId),
                productID: parseInt(productId)
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
                throw new Error(responseData.message || 'Error al crear reseña');
            }
    
            showAlert('Reseña creada exitosamente', 'success');
            document.getElementById('addReviewForm').reset();
            await loadReviews();
            bootstrap.Modal.getInstance(document.getElementById('addReviewModal')).hide();
        } catch (error) {
            showAlert('Error al crear reseña: ' + error.message, 'danger');
            console.error('Error detallado:', error);
        } finally {
            showLoading(false);
        }
    });

    // Editar reseña (cargar datos en modal)
    async function loadReviewForEdit(reviewId) {
        try {
            showLoading(true);
            const response = await fetch(`${apiUrl}${reviewId}`);
            
            if (!response.ok) {
                throw new Error('Error al cargar reseña para editar');
            }
            
            const review = await response.json();
            
            // Llenar el formulario de edición
            document.getElementById('editReviewId').value = review.reviewID;
            document.getElementById('editRating').value = review.rating;
            document.getElementById('editComment').value = review.comment;
            const editUserNameInput = document.getElementById('editUserName');
if (editUserNameInput) editUserNameInput.value = `Usuario #${review.user.userID}`;

const editUserIdInput = document.getElementById('editUserId');
if (editUserIdInput) editUserIdInput.value = review.user.userID;

const editProductNameInput = document.getElementById('editProductName');
if (editProductNameInput) editProductNameInput.value = `Producto #${review.product.productID}`;

const editProductIdInput = document.getElementById('editProductId');
if (editProductIdInput) editProductIdInput.value = review.product.productID;
            
            // Mostrar el modal
            const editModal = new bootstrap.Modal(document.getElementById('editReviewModal'));
            editModal.show();
            
        } catch (error) {
            showAlert('Error al cargar reseña: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }
    
    // Actualizar reseña
    document.getElementById('editReviewForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const reviewId = document.getElementById('editReviewId').value;
        const rating = document.getElementById('editRating').value;
        const comment = document.getElementById('editComment').value;
        const userId = document.getElementById('editUserId').value;
        const productId = document.getElementById('editProductId').value;
        
        try {
            showLoading(true);
            
            const requestBody = {
                rating: parseInt(rating),
                comment: comment,
                userID: parseInt(userId),
                productID: parseInt(productId)
            };
    
            const response = await fetch(`${apiUrl}${reviewId}`, {
                method: 'PUT',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });
    
            const responseData = await response.json();
    
            if (!response.ok) {
                throw new Error(responseData.message || 'Error al actualizar reseña');
            }
    
            showAlert('Reseña actualizada exitosamente', 'success');
            await loadReviews();
            bootstrap.Modal.getInstance(document.getElementById('editReviewModal')).hide();
        } catch (error) {
            showAlert('Error al actualizar reseña: ' + error.message, 'danger');
            console.error('Error detallado:', error);
        } finally {
            showLoading(false);
        }
    });
    

    // Eliminar reseña
    async function deleteReview(reviewId) {
        if (!confirm('¿Está seguro de eliminar esta reseña?')) return;
        
        try {
            showLoading(true);
            const response = await fetch(`${apiUrl}${reviewId}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Error al eliminar reseña');
            }
            
            showAlert('Reseña eliminada correctamente', 'success');
            await loadReviews();
        } catch (error) {
            showAlert('Error al eliminar reseña: ' + error.message, 'danger');
        } finally {
            showLoading(false);
        }
    }

    // Agregar event listeners a los botones
    function addButtonListeners() {
        // Botones de editar
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', function() {
                const reviewId = this.getAttribute('data-review-id');
                loadReviewForEdit(reviewId);
            });
        });
        
        // Botones de eliminar
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function() {
                const reviewId = this.getAttribute('data-review-id');
                deleteReview(reviewId);
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
    document.getElementById('ratingFilter').addEventListener('input', filterReviews);
    document.getElementById('commentFilter').addEventListener('input', filterReviews);
    document.getElementById('userFilter').addEventListener('input', filterReviews);
    document.getElementById('productFilter').addEventListener('input', filterReviews);

    // Inicialización
    loadReviews();
    loadUsersAndProducts();
});