document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput'); // Campo de búsqueda por nombre
    const roleFilter = document.getElementById('roleFilter'); // Filtro de roles
    const statusFilter = document.getElementById('statusFilter'); // Filtro de estado
    const tableBody = document.getElementById('usersTableBody'); // Tabla de usuarios
    const searchButton = document.querySelector('.btn-primary'); // Botón de buscar
    const apiUrlUsers = 'http://localhost:8080/api/v1/user/'; // URL de la API para usuarios
    const apiUrlRoles = 'http://localhost:8080/api/v1/role/'; // URL de la API para roles

    let allUsers = []; // Variable para almacenar todos los usuarios

    // Función para cargar los usuarios desde la API
    async function loadUsers() {
        try {
            const response = await fetch(apiUrlUsers);

            if (!response.ok) {
                throw new Error("Error al obtener los usuarios.");
            }

            allUsers = await response.json();
            displayUsers(allUsers); // Mostrar usuarios en la tabla
        } catch (error) {
            console.error("Error al cargar los usuarios:", error);
            alert("No se pudieron cargar los usuarios. Intente nuevamente más tarde.");
        }
    }

    // Función para mostrar usuarios en la tabla
    function displayUsers(users) {
        tableBody.innerHTML = ''; // Limpiar el contenido previo

        users.forEach(user => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${user.userID}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.status ? "Activo" : "Inactivo"}</td>
                <td>${user.roleID.name}</td>
                <td>
                    <button class="btn btn-sm btn-primary me-1" onclick="editUser(${user.userID})" title="Editar">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-danger me-1" onclick="deleteUser(${user.userID})" title="Eliminar">
                        <i class="fa fa-trash"></i>
                    </button>
                    ${!user.status 
                        ? `<button class="btn btn-sm btn-success me-1" onclick="reactivateUser(${user.userID})" title="Reactivar">
                            <i class="fa fa-arrow-up"></i>
                           </button>` 
                        : ''}
                </td>
            `;

            tableBody.appendChild(row);
        });
    }

    // Función para cargar los roles en el filtro de roles
    async function loadRolesForFilter() {
        try {
            const response = await fetch(apiUrlRoles);

            if (!response.ok) {
                throw new Error("Error al obtener los roles.");
            }

            const roles = await response.json();
            roleFilter.innerHTML = '<option value="">Todos los roles</option>'; // Limpiar opciones previas y agregar "Todos los roles"

            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.roleID; // Usar el ID del rol como valor
                option.textContent = role.name; // Mostrar el nombre del rol
                roleFilter.appendChild(option);
            });
        } catch (error) {
            console.error("Error al cargar los roles:", error);
            alert("No se pudieron cargar los roles. Intente nuevamente más tarde.");
        }
    }

    // Función para aplicar los filtros de estado y roles en tiempo real
    function applyFilters() {
        const selectedRoleID = roleFilter.value; // Obtener el rol seleccionado
        const selectedStatus = statusFilter.value; // Obtener el estado seleccionado

        const filteredUsers = allUsers.filter(user => {
            const roleMatch = selectedRoleID ? user.roleID.roleID.toString() === selectedRoleID : true;
            const statusMatch = selectedStatus
                ? (selectedStatus === "activo" && user.status) || (selectedStatus === "inactivo" && !user.status)
                : true;

            return roleMatch && statusMatch;
        });

        displayUsers(filteredUsers); // Mostrar los usuarios filtrados
    }

    // Función para enviar la solicitud de búsqueda al backend (solo para el campo de nombre)
    async function searchUsers() {
        const searchQuery = searchInput.value.trim(); // Texto de búsqueda

        let queryParams = [];

        // Construir los parámetros de consulta (query string)
        if (searchQuery) queryParams.push(`name=${encodeURIComponent(searchQuery)}`);

        const queryString = queryParams.length > 0 ? `?${queryParams.join('&')}` : '';

        try {
            const response = await fetch(`${apiUrlUsers}filter${queryString}`);

            if (!response.ok) {
                throw new Error("Error al obtener los usuarios.");
            }

            const users = await response.json();
            displayUsers(users); // Mostrar usuarios en la tabla
        } catch (error) {
            console.error("Error al buscar los usuarios:", error);
            alert("No se pudo realizar la búsqueda. Intente nuevamente más tarde.");
        }
    }

    // Función para activar o desactivar usuarios
    window.toggleUserStatus = async function (userId, isActive) {
        const confirmAction = confirm(`¿Estás seguro de que deseas ${isActive ? "activar" : "desactivar"} este usuario?`);
        if (!confirmAction) return;

        try {
            const response = await fetch(`${apiUrlUsers}${userId}`, {
                method: "PATCH", // Usamos PATCH para actualizar parcialmente el usuario
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ status: isActive }) // Cambiamos el estado del usuario
            });

            if (response.ok) {
                alert(`Usuario ${isActive ? "activado" : "desactivado"} correctamente.`);
                loadUsers(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || `Error al ${isActive ? "activar" : "desactivar"} el usuario.`);
            }
        } catch (error) {
            console.error(`Error al ${isActive ? "activar" : "desactivar"} el usuario:`, error);
            alert(`No se pudo ${isActive ? "activar" : "desactivar"} el usuario. Intente nuevamente más tarde.`);
        }
    };

    // Función para reactivar usuarios
    window.reactivateUser = async function (userId) {
        const confirmAction = confirm("¿Estás seguro de que deseas reactivar este usuario?");
        if (!confirmAction) return;

        try {
            const response = await fetch(`${apiUrlUsers}${userId}/reactivate`, {
                method: "PATCH", // Usamos PATCH para reactivar el usuario
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (response.ok) {
                alert("Usuario reactivado correctamente.");
                loadUsers(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al reactivar el usuario.");
            }
        } catch (error) {
            console.error("Error al reactivar el usuario:", error);
            alert("No se pudo reactivar el usuario. Intente nuevamente más tarde.");
        }
    };

    // Función para editar usuarios
    window.editUser = async function (userId) {
        const apiUrl = `${apiUrlUsers}${userId}`; // Endpoint para obtener datos del usuario
        try {
            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error("Error al obtener los datos del usuario.");
            }

            const user = await response.json();

            // Llenar los campos del modal con los datos del usuario
            document.getElementById('editUserID').value = user.userID;
            document.getElementById('editUserName').value = user.name;
            document.getElementById('editUserEmail').value = user.email;

            const roleSelect = document.getElementById('editUserRole');
            roleSelect.innerHTML = '<option value="">Seleccione un rol</option>'; // Limpiar roles previos

            // Obtener roles desde la API
            const rolesResponse = await fetch(apiUrlRoles);
            const roles = await rolesResponse.json();

            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role.roleID;
                option.textContent = role.name;

                if (role.roleID === user.roleID.roleID) {
                    option.selected = true;
                }

                roleSelect.appendChild(option);
            });

            // Mostrar el modal de edición
            const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
            editUserModal.show();
        } catch (error) {
            console.error("Error al cargar los datos del usuario:", error);
            alert("No se pudieron cargar los datos del usuario.");
        }
    };

    // Función para guardar los cambios realizados en el modal
    document.getElementById('editUserForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const userID = document.getElementById('editUserID').value;
        const name = document.getElementById('editUserName').value.trim();
        const email = document.getElementById('editUserEmail').value.trim();
        const roleID = document.getElementById('editUserRole').value;

        if (!name || !email || !roleID) {
            alert("Por favor, complete todos los campos.");
            return;
        }

        const bodyContent = JSON.stringify({ name, email, roleID });

        try {
            const response = await fetch(`${apiUrlUsers}${userID}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: bodyContent
            });

            if (response.ok) {
                alert("Usuario actualizado correctamente.");
                const editUserModal = bootstrap.Modal.getInstance(document.getElementById('editUserModal'));
                editUserModal.hide(); // Cerrar modal
                loadUsers(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al actualizar el usuario.");
            }
        } catch (error) {
            console.error("Error al actualizar el usuario:", error);
            alert("No se pudo actualizar el usuario. Intente nuevamente más tarde.");
        }
    });

    // Función para eliminar usuarios
    window.deleteUser = async function (userId) {
        const confirmDelete = confirm("¿Estás seguro de que deseas eliminar este usuario?");
        if (!confirmDelete) return;

        try {
            const response = await fetch(`${apiUrlUsers}${userId}`, {
                method: "DELETE"
            });

            if (response.ok) {
                alert("Usuario eliminado correctamente.");
                loadUsers(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al eliminar el usuario.");
            }
        } catch (error) {
            console.error("Error al eliminar el usuario:", error);
            alert("No se pudo eliminar el usuario. Intente nuevamente más tarde.");
        }
    };

    // Escuchar evento de clic en el botón "Buscar" (para el campo de nombre)
    searchButton.addEventListener('click', searchUsers);

    // Escuchar eventos de cambio en los filtros de estado y roles (en tiempo real)
    roleFilter.addEventListener('change', applyFilters);
    statusFilter.addEventListener('change', applyFilters);

    // Cargar roles y usuarios al cargar la página
    loadRolesForFilter();
    loadUsers();
});