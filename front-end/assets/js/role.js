document.addEventListener('DOMContentLoaded', function () {
    // Función para cargar roles desde la API y rellenar la tabla
    async function loadRoles() {
        const apiUrl = 'http://localhost:8080/api/v1/role/'; // Cambia esta URL a la de tu API
        const tableBody = document.getElementById('rolesTableBody');

        // Validar si el elemento tabla existe
        if (!tableBody) {
            console.error("El elemento de tabla para roles no existe.");
            return;
        }

        try {
            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error("Error al obtener los roles.");
            }

            let roles = await response.json();

            // Validar que los datos tengan los campos necesarios
            if (!Array.isArray(roles)) {
                throw new Error("La respuesta de la API no es una lista de roles válida.");
            }

            // Ordenar los roles por roleID en orden ascendente
            roles.sort((a, b) => a.roleID - b.roleID);

            // Limpiar el contenido previo de la tabla
            tableBody.innerHTML = '';

            // Recorrer los roles y generar filas dinámicas
            roles.forEach(role => {
                // Validar que roleID y roleName existan
                const roleID = role.roleID !== undefined ? role.roleID : "ID no definido";
                const roleName = role.name !== undefined ? role.name : "Nombre no definido";

                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${roleID}</td>
                    <td>${roleName}</td>
                    <td>
                        <button class="btn btn-sm btn-primary me-1" id="edit-btn-${roleID}" onclick="editRole(${roleID})">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" id="delete-btn-${roleID}" onclick="deleteRole(${roleID})">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                `;

                // Agregar la fila al cuerpo de la tabla
                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error("Error al cargar los roles:", error);
            alert("No se pudieron cargar los roles. Intente nuevamente más tarde.");
        }
    }

    // Función para abrir el modal de edición y cargar los datos del rol
    window.editRole = async function (roleID) {
        const apiUrl = `http://localhost:8080/api/v1/role/${roleID}`; // Endpoint para obtener datos del rol
        try {
            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error("Error al obtener los datos del rol.");
            }

            const role = await response.json();

            // Llenar los campos del modal con los datos del rol
            document.getElementById('editRoleID').value = role.roleID;
            document.getElementById('editRoleName').value = role.name;

            // Mostrar el modal de edición
            const editRoleModal = new bootstrap.Modal(document.getElementById('editRoleModal'));
            editRoleModal.show();
        } catch (error) {
            console.error("Error al cargar los datos del rol:", error);
            alert("No se pudieron cargar los datos del rol.");
        }
    };

    // Función para guardar los cambios realizados en el modal
    document.getElementById('editRoleForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const roleID = document.getElementById('editRoleID').value;
        const roleName = document.getElementById('editRoleName').value.trim();

        if (!roleName) {
            alert("Por favor, ingrese un nombre para el rol.");
            return;
        }

        const bodyContent = JSON.stringify({ roleName });

        try {
            const response = await fetch(`http://localhost:8080/api/v1/role/${roleID}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: bodyContent
            });

            if (response.ok) {
                alert("Rol actualizado correctamente.");
                document.getElementById('editRoleModal').querySelector('.btn-close').click(); // Cerrar modal
                loadRoles(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al actualizar el rol.");
            }
        } catch (error) {
            console.error("Error al actualizar el rol:", error);
            alert("No se pudo actualizar el rol. Intente nuevamente más tarde.");
        }
    });

    // Función para registrar un nuevo rol
    document.getElementById('registerRoleForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const roleName = document.getElementById('registerRoleName').value.trim();

        if (!roleName) {
            alert("Por favor, ingrese un nombre para el rol.");
            return;
        }

        const bodyContent = JSON.stringify({ roleName });

        try {
            const response = await fetch('http://localhost:8080/api/v1/role/', {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: bodyContent
            });

            if (response.ok) {
                alert("Rol registrado correctamente.");
                document.getElementById('addRoleModal').querySelector('.btn-close').click(); // Cerrar modal
                loadRoles(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al registrar el rol.");
            }
        } catch (error) {
            console.error("Error al registrar el rol:", error);
            alert("No se pudo registrar el rol. Intente nuevamente más tarde.");
        }
    });

    // Función para eliminar un rol
    window.deleteRole = async function (roleID) {
        const confirmDelete = confirm("¿Estás seguro de que deseas eliminar este rol?");
        if (!confirmDelete) return;

        try {
            const response = await fetch(`http://localhost:8080/api/v1/role/${roleID}`, {
                method: "DELETE",
            });

            if (response.ok) {
                alert("Rol eliminado correctamente.");
                loadRoles(); // Recargar la tabla
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al eliminar el rol.");
            }
        } catch (error) {
            console.error("Error al eliminar el rol:", error);
            alert("No se pudo eliminar el rol. Intente nuevamente más tarde.");
        }
    };

    // Cargar roles al cargar la página
    loadRoles();
});