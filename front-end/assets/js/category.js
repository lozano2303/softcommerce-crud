document.addEventListener('DOMContentLoaded', function() {
    // Función para cargar todas las categorías
    async function loadCategories() {
        try {
            const response = await fetch('http://localhost:8080/api/v1/category/');
            
            if (!response.ok) {
                throw new Error('Error al obtener las categorías');
            }

            const categories = await response.json();
            const tableBody = document.getElementById('categoriesTableBody');
            
            // Limpiar la tabla antes de cargar nuevos datos
            tableBody.innerHTML = '';
            
            // Ordenar categorías por ID
            categories.sort((a, b) => a.categoryID - b.categoryID);
            
            // Llenar la tabla con las categorías
            categories.forEach(category => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${category.categoryID}</td>
                    <td>${category.categoryName}</td>
                    <td>
                        <button class="btn btn-sm btn-primary me-1" onclick="editCategory(${category.categoryID})">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteCategory(${category.categoryID})">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error al cargar categorías:', error);
            alert('No se pudieron cargar las categorías. Intente nuevamente más tarde.');
        }
    }

    // Función para editar una categoría
    window.editCategory = async function(categoryID) {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/category/${categoryID}`);
            
            if (!response.ok) {
                throw new Error('Error al obtener los datos de la categoría');
            }

            const category = await response.json();
            
            // Llenar el formulario de edición con los datos de la categoría
            document.getElementById('editCategoryID').value = category.categoryID;
            document.getElementById('editCategoryName').value = category.categoryName;
            
            // Mostrar el modal de edición
            const editModal = new bootstrap.Modal(document.getElementById('editCategoryModal'));
            editModal.show();
        } catch (error) {
            console.error('Error al cargar categoría para editar:', error);
            alert('No se pudo cargar la categoría para editar.');
        }
    };

    // Evento para guardar cambios al editar
    document.getElementById('editCategoryForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const categoryID = document.getElementById('editCategoryID').value;
        const categoryName = document.getElementById('editCategoryName').value.trim();
        
        if (!categoryName) {
            alert('Por favor ingrese un nombre para la categoría');
            return;
        }
        
        try {
            const response = await fetch(`http://localhost:8080/api/v1/category/${categoryID}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ categoryName })
            });
            
            if (response.ok) {
                alert('Categoría actualizada correctamente');
                
                // Cerrar el modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('editCategoryModal'));
                modal.hide();
                
                // Recargar la tabla
                loadCategories();
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Error al actualizar la categoría');
            }
        } catch (error) {
            console.error('Error al actualizar categoría:', error);
            alert('No se pudo actualizar la categoría. Intente nuevamente más tarde.');
        }
    });

    // Evento para registrar nueva categoría
    document.getElementById('registerCategoryForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const categoryName = document.getElementById('registerCategoryName').value.trim();
        
        if (!categoryName) {
            alert('Por favor ingrese un nombre para la categoría');
            return;
        }
        
        try {
            const response = await fetch('http://localhost:8080/api/v1/category/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ categoryName })
            });
            
            if (response.ok) {
                alert('Categoría registrada correctamente');
                
                // Cerrar el modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('addCategoryModal'));
                modal.hide();
                
                // Limpiar el formulario
                this.reset();
                
                // Recargar la tabla
                loadCategories();
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Error al registrar la categoría');
            }
        } catch (error) {
            console.error('Error al registrar categoría:', error);
            alert('No se pudo registrar la categoría. Intente nuevamente más tarde.');
        }
    });

    // Función para eliminar una categoría
    window.deleteCategory = async function(categoryID) {
        if (!confirm('¿Está seguro que desea eliminar esta categoría?')) {
            return;
        }
        
        try {
            const response = await fetch(`http://localhost:8080/api/v1/category/${categoryID}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                alert('Categoría eliminada correctamente');
                loadCategories(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Error al eliminar la categoría');
            }
        } catch (error) {
            console.error('Error al eliminar categoría:', error);
            alert('No se pudo eliminar la categoría. Intente nuevamente más tarde.');
        }
    };

    // Cargar categorías al iniciar la página
    loadCategories();
});