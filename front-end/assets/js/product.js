document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput'); // Campo de búsqueda por nombre
    const categoryFilter = document.getElementById('categoryFilter'); // Filtro de categorías
    const statusFilter = document.getElementById('statusFilter'); // Filtro de estado
    const tableBody = document.getElementById('productsTableBody'); // Tabla de productos
    const searchButton = document.querySelector('.btn-primary'); // Botón de buscar
    const apiUrlProducts = 'http://localhost:8080/api/v1/products/'; // URL de la API para productos
    const apiUrlCategories = 'http://localhost:8080/api/v1/category/'; // URL de la API para categorías

    let allProducts = []; // Variable para almacenar todos los productos
    let allCategories = []; // Variable para almacenar todas las categorías

    // Función para cargar los productos desde la API
    async function loadProducts() {
        try {
            const response = await fetch(apiUrlProducts);

            if (!response.ok) {
                throw new Error("Error al obtener los productos.");
            }

            allProducts = await response.json();
            displayProducts(allProducts); // Mostrar productos en la tabla
        } catch (error) {
            console.error("Error al cargar los productos:", error);
            alert("No se pudieron cargar los productos. Intente nuevamente más tarde.");
        }
    }

    // Función para mostrar productos en la tabla
    function displayProducts(products) {
        const tableBody = document.getElementById('productsTableBody');
        tableBody.innerHTML = ''; // Limpiar el contenido previo
    
        products.forEach(product => {
            const row = document.createElement('tr');
    
            // Verificar si la URL es válida. Si no, usar una imagen predeterminada.
            const imageUrl = product.imageUrl && product.imageUrl.startsWith('http')
                ? product.imageUrl
                : 'assets/img/default-product.png';
    
            // Validar y mostrar la categoría del producto
            const categoryName = product.category && product.category.categoryName
                ? product.category.categoryName
                : "Sin categoría";
    
            // Determinar el estado basado en el campo `status`
            const statusText = product.status ? 'Activo' : 'Inactivo';
            const statusClass = product.status ? 'text-success' : 'text-danger';
    
            // Construir la fila de la tabla
            row.innerHTML = `
                <td>${product.productID}</td>
                <td>
                    <img src="${imageUrl}" class="product-img" alt="Imagen del producto" 
                         onerror="this.src='assets/img/default-product.png';">
                </td>
                <td>${product.name}</td>
                <td>${categoryName}</td>
                <td>$${product.price.toFixed(2)}</td>
                <td>${product.stock}</td>
                <td><span class="${statusClass}">${statusText}</span></td>
                <td>
                    <button class="btn btn-sm btn-primary me-1" onclick="editProduct(${product.productID})" title="Editar">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger me-1" onclick="deleteProduct(${product.productID})" title="Eliminar">
                        <i class="fa fa-trash"></i>
                    </button>
                    ${!product.status ? `
                        <button class="btn btn-sm btn-success me-1" onclick="reactivateProduct(${product.productID})" title="Reactivar">
                            <i class="fa fa-check"></i>
                        </button>
                    ` : ''}
                </td>
            `;
    
            tableBody.appendChild(row);
        });
    }

    // Función para cargar las categorías en los menús desplegables
    async function loadCategoriesForMenus() {
        try {
            const response = await fetch(apiUrlCategories);
    
            if (!response.ok) {
                throw new Error("Error al obtener las categorías.");
            }
    
            allCategories = await response.json();
    
            // Llenar el filtro de categorías
            categoryFilter.innerHTML = '<option value="">Todas las categorías</option>'; // Opción "Todas las categorías"
    
            // Llenar los selects de categorías en los modales
            const categorySelects = [
                document.getElementById('productCategory'),
                document.getElementById('editProductCategory')
            ];
    
            allCategories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.categoryID;
                option.textContent = category.categoryName;
    
                // Agregar al filtro de categorías
                categoryFilter.appendChild(option.cloneNode(true));
    
                // Agregar a los selects de los modales
                categorySelects.forEach(select => {
                    if (select) {
                        select.appendChild(option.cloneNode(true));
                    }
                });
            });
        } catch (error) {
            console.error("Error al cargar las categorías:", error);
            alert("No se pudieron cargar las categorías. Intente nuevamente más tarde.");
        }
    }

    // Función para aplicar los filtros de categoría y estado
    function applyFilters() {
        const selectedCategoryID = categoryFilter.value; // Obtener la categoría seleccionada
        const selectedStatus = statusFilter.value; // Obtener el estado seleccionado
    
        const filteredProducts = allProducts.filter(product => {
            // Coincidencia de categoría
            const categoryMatch = selectedCategoryID 
                ? product.category?.categoryID.toString() === selectedCategoryID 
                : true;
    
            // Coincidencia de estado (activo/inactivo)
            const statusMatch = selectedStatus
                ? (selectedStatus === "activo" && product.status) || (selectedStatus === "inactivo" && !product.status)
                : true;
    
            return categoryMatch && statusMatch;
        });
    
        displayProducts(filteredProducts); // Mostrar los productos filtrados
    }

    // Función para buscar productos por nombre
    async function searchProducts() {
        const searchQuery = searchInput.value.trim(); // Texto de búsqueda

        let queryParams = [];
        if (searchQuery) queryParams.push(`name=${encodeURIComponent(searchQuery)}`);

        const queryString = queryParams.length > 0 ? `?${queryParams.join('&')}` : '';

        try {
            const response = await fetch(`${apiUrlProducts}filter${queryString}`);

            if (!response.ok) {
                throw new Error("Error al buscar los productos.");
            }

            const products = await response.json();
            displayProducts(products); // Mostrar productos en la tabla
        } catch (error) {
            console.error("Error al buscar los productos:", error);
            alert("No se pudo realizar la búsqueda. Intente nuevamente más tarde.");
        }
    }

    // Función para agregar un nuevo producto
    document.getElementById('addProductForm').addEventListener('submit', async function (e) {
        e.preventDefault();
    
        // Capturar los valores del formulario
        const name = document.getElementById('productName').value.trim();
        const description = document.getElementById('productDescription').value.trim();
        const price = parseFloat(document.getElementById('productPrice').value);
        const stock = parseInt(document.getElementById('productStock').value);
        const categoryID = document.getElementById('productCategory').value;
        const image_url = document.getElementById('productImageUrl').value.trim(); // Cambiar a image_url
    
        // Validar que todos los campos estén completos
        if (!name || !description || !price || !stock || !categoryID || !image_url) {
            alert("Por favor complete todos los campos.");
            return;
        }
    
        // Crear el objeto con image_url
        const productData = {
            name,
            description,
            price,
            stock,
            categoryID,
            image_url // Usar snake_case para el campo de imagen
        };
    
        try {
            // Realizar la solicitud POST al backend
            const response = await fetch('http://localhost:8080/api/v1/products/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(productData)
            });
    
            if (response.ok) {
                alert("Producto agregado correctamente.");
                document.getElementById('addProductForm').reset(); // Limpiar el formulario
                loadProducts(); // Recargar la lista de productos
                closeModal(); // Cerrar el modal
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al agregar el producto.");
            }
        } catch (error) {
            console.error("Error al agregar producto:", error);
            alert("No se pudo agregar el producto. Intente nuevamente más tarde.");
        }
    });

    function closeModal() {
        const modalElement = document.getElementById('addProductModal'); // ID del modal
        const modalInstance = bootstrap.Modal.getInstance(modalElement); // Obtén la instancia del modal
        modalInstance.hide(); // Cierra el modal
    }

    // Función para editar un producto
    window.editProduct = async function (productID) {
        try {
            // Obtener el producto por ID
            const response = await fetch(`${apiUrlProducts}${productID}`);
            if (!response.ok) {
                throw new Error("Error al obtener los datos del producto.");
            }
    
            const product = await response.json();
            console.log("Producto recibido del backend:", product); // Depuración
    
            // Llenar el formulario de edición con verificaciones de elementos
            const editProductIdField = document.getElementById('editProductId');
            const editProductNameField = document.getElementById('editProductName');
            const editProductDescriptionField = document.getElementById('editProductDescription');
            const editProductPriceField = document.getElementById('editProductPrice');
            const editProductStockField = document.getElementById('editProductStock');
            const editProductImageUrlField = document.getElementById('editProductImageUrl'); // imageUrl
            const categorySelect = document.getElementById('editProductCategory');
    
            // Asignar valores al formulario
            if (editProductIdField) editProductIdField.value = product.productID;
            if (editProductNameField) editProductNameField.value = product.name;
            if (editProductDescriptionField) editProductDescriptionField.value = product.description;
            if (editProductPriceField) editProductPriceField.value = product.price;
            if (editProductStockField) editProductStockField.value = product.stock;
    
            // Asignar el valor de imageUrl al campo del formulario
            if (editProductImageUrlField) {
                editProductImageUrlField.value = product.imageUrl || ""; // Usar el nombre correcto (camelCase)
                console.log("Valor asignado a imageUrl:", editProductImageUrlField.value); // Depuración
            }
    
            // Manejo de categorías
            if (categorySelect) {
                categorySelect.innerHTML = '<option value="">Seleccione una categoría</option>'; // Limpiar categorías previas
    
                const categoriesResponse = await fetch(apiUrlCategories);
                if (!categoriesResponse.ok) {
                    throw new Error("Error al obtener las categorías.");
                }
    
                const categories = await categoriesResponse.json();
    
                categories.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.categoryID;
                    option.textContent = category.categoryName;
    
                    // Seleccionar la categoría correspondiente al producto
                    if (category.categoryID === product.category?.categoryID) {
                        option.selected = true;
                    }
    
                    categorySelect.appendChild(option);
                });
            }
    
            // Mostrar el modal de edición
            const editProductModal = new bootstrap.Modal(document.getElementById('editProductModal'));
            editProductModal.show();
        } catch (error) {
            console.error("Error al cargar los datos del producto:", error);
            alert("No se pudieron cargar los datos del producto. Por favor, intente nuevamente.");
        }
    };
// Función para guardar los cambios realizados al producto
document.getElementById('editProductForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const productID = document.getElementById('editProductId').value;
    const name = document.getElementById('editProductName').value.trim();
    const description = document.getElementById('editProductDescription').value.trim();
    const price = parseFloat(document.getElementById('editProductPrice').value);
    const stock = parseInt(document.getElementById('editProductStock').value);
    const categoryID = parseInt(document.getElementById('editProductCategory').value);
    const imageUrl = document.getElementById('editProductImageUrl').value.trim();

    // Validar `imageUrl`
    if (!imageUrl || !imageUrl.startsWith('http')) {
        alert("Por favor, introduzca una URL válida para la imagen.");
        return;
    }

    // Validaciones generales
    if (!name || !description || isNaN(price) || isNaN(stock) || !categoryID) {
        alert("Por favor, complete todos los campos.");
        return;
    }

    // Cambiar el nombre de la clave a "image_url" para que coincida con el backend
    const productData = { 
        name, 
        description, 
        price, 
        stock, 
        categoryID, 
        image_url: imageUrl // Enviar como image_url
    };

    console.log("Datos enviados:", productData); // Depuración

    try {
        const response = await fetch(`${apiUrlProducts}${productID}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(productData)
        });

        if (response.ok) {
            alert("Producto actualizado correctamente.");
            const editProductModal = bootstrap.Modal.getInstance(document.getElementById('editProductModal'));
            editProductModal.hide(); // Cerrar modal
            loadProducts(); // Recargar los productos
        } else {
            const errorData = await response.json();
            console.log("Respuesta del servidor en error:", errorData); // Depuración
            alert(errorData.message || "Error al actualizar el producto.");
        }
    } catch (error) {
        console.error("Error al actualizar el producto:", error);
        alert("No se pudo actualizar el producto. Intente nuevamente más tarde.");
    }
});

    // Función para guardar los cambios realizados al producto
    document.getElementById('editProductForm').addEventListener('submit', async function (e) {
        e.preventDefault();
    
        const productID = document.getElementById('editProductId').value;
        const name = document.getElementById('editProductName').value.trim();
        const description = document.getElementById('editProductDescription').value.trim();
        const price = parseFloat(document.getElementById('editProductPrice').value);
        const stock = parseInt(document.getElementById('editProductStock').value);
        const categoryID = parseInt(document.getElementById('editProductCategory').value);
        const imageUrl = document.getElementById('editProductImageUrl').value.trim();
    
        // Validar `imageUrl`
        if (!imageUrl || !imageUrl.startsWith('http')) {
            alert("Por favor, introduzca una URL válida para la imagen.");
            return;
        }
    
        // Validaciones generales
        if (!name || !description || isNaN(price) || isNaN(stock) || !categoryID) {
            alert("Por favor, complete todos los campos.");
            return;
        }
    
        const productData = { name, description, price, stock, categoryID, imageUrl };
        console.log("Datos enviados:", productData); // Depuración
    
        try {
            const response = await fetch(`${apiUrlProducts}${productID}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(productData)
            });
    
            if (response.ok) {
                alert("Producto actualizado correctamente.");
                const editProductModal = bootstrap.Modal.getInstance(document.getElementById('editProductModal'));
                editProductModal.hide(); // Cerrar modal
                loadProducts(); // Recargar los productos
            } else {
                const errorData = await response.json();
                console.log("Respuesta del servidor en error:", errorData); // Depuración
                alert(errorData.message || "Error al actualizar el producto.");
            }
        } catch (error) {
            console.error("Error al actualizar el producto:", error);
            alert("No se pudo actualizar el producto. Intente nuevamente más tarde.");
        }
    });

    // Función para eliminar un producto
    window.deleteProduct = async function (productID) {
        const confirmDelete = confirm("¿Estás seguro de que deseas eliminar este producto?");
        if (!confirmDelete) return;

        try {
            const response = await fetch(`${apiUrlProducts}${productID}`, {
                method: "DELETE"
            });

            if (response.ok) {
                alert("Producto eliminado correctamente.");
                loadProducts(); // Recargar los productos
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al eliminar el producto.");
            }
        } catch (error) {
            console.error("Error al eliminar el producto:", error);
            alert("No se pudo eliminar el producto. Intente nuevamente más tarde.");
        }
    };

    // Función para reactivar un producto
    window.reactivateProduct = async function (productId) {
        const confirmAction = confirm("¿Estás seguro de que deseas reactivar este producto?");
        if (!confirmAction) return;
    
        try {
            const response = await fetch(`${apiUrlProducts}${productId}/reactivate`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                }
            });
    
            if (response.ok) {
                alert("Producto reactivado correctamente.");
                loadProducts(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al reactivar el producto.");
            }
        } catch (error) {
            console.error("Error al reactivar el producto:", error);
            alert("No se pudo reactivar el producto. Intente nuevamente más tarde.");
        }
    };

    // Escuchar evento de clic en el botón "Buscar"
    searchButton.addEventListener('click', searchProducts);

    // Escuchar cambios en los filtros de categoría y estado
    categoryFilter.addEventListener('change', applyFilters);
    statusFilter.addEventListener('change', applyFilters);

    // Cargar categorías y productos al cargar la página
    loadCategoriesForMenus();
    loadProducts();
});