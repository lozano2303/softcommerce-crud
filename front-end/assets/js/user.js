document.addEventListener('DOMContentLoaded', function () {
    // Función para cargar usuarios desde la API y rellenar la tabla
    async function loadUsers() {
        const apiUrl = 'http://localhost:8080/api/v1/user/'; // Cambia esta URL a la de tu API
        const tableBody = document.getElementById('usersTableBody');

        if (!tableBody) {
            console.error("El elemento de tabla para usuarios no existe.");
            return;
        }

        try {
            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error("Error al obtener los usuarios.");
            }

            const users = await response.json();
            tableBody.innerHTML = ''; // Limpiar el contenido previo

            // Recorrer los usuarios y generar filas dinámicas
            users.forEach(user => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${user.userID}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.status ? "Activo" : "Inactivo"}</td> <!-- Mostrar el estado como Activo/Inactivo -->
                    <td>${user.roleID.name}</td> <!-- Mostrar solo el nombre del rol -->
                    <td>
                        <button class="btn btn-sm btn-primary me-1" id="edit-btn-${user.userID}" onclick="editUser(${user.userID})">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" id="delete-btn-${user.userID}" onclick="deleteUser(${user.userID})">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                `;

                // Agregar la fila al cuerpo de la tabla
                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error("Error al cargar los usuarios:", error);
            alert("No se pudieron cargar los usuarios. Intente nuevamente más tarde.");
        }
    }

    // Declarar editUser en el ámbito global
    window.editUser = async function (userId) {
        const apiUrl = `http://localhost:8080/api/v1/user/${userId}`; // Endpoint para obtener datos del usuario
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
            const rolesResponse = await fetch('http://localhost:8080/api/v1/role/');
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

    // Declarar deleteUser en el ámbito global
    window.deleteUser = async function (userId) {
        const confirmDelete = confirm("¿Estás seguro de que deseas eliminar este usuario?");
        if (!confirmDelete) return;

        try {
            const response = await fetch(`http://localhost:8080/api/v1/user/${userId}`, {
                method: "DELETE",
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
            const response = await fetch(`http://localhost:8080/api/v1/user/${userID}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: bodyContent
            });

            if (response.ok) {
                alert("Usuario actualizado correctamente.");
                document.getElementById('editUserModal').querySelector('.btn-close').click(); // Cerrar modal
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

    // Cargar usuarios al cargar la página
    loadUsers();
});