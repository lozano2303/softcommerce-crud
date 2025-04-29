document.addEventListener('DOMContentLoaded', function () {
    const statusFilter = document.getElementById('statusFilter'); // Filtro de estado
    const apiUrlOrders = 'http://localhost:8080/api/v1/order/'; // URL de la API para órdenes
    const apiUrlUsers = 'http://localhost:8080/api/v1/user/'; // URL de la API para usuarios

    let allOrders = []; // Variable para almacenar todas las órdenes
    let allUsers = []; // Variable para almacenar todos los usuarios


// Función para cargar órdenes desde la API
async function loadOrders() {
    try {
        const response = await fetch(apiUrlOrders);

        if (!response.ok) {
            throw new Error("Error al obtener las órdenes.");
        }

        allOrders = await response.json(); // Actualizar la variable global
        displayOrders(allOrders); // Mostrar las órdenes en la tabla
    } catch (error) {
        console.error("Error al cargar las órdenes:", error);
        alert("No se pudieron cargar las órdenes. Intente nuevamente más tarde.");
    }
}

// Función para mostrar las órdenes en la tabla
function displayOrders(orders) {
    const tableBody = document.getElementById('ordersTableBody');
    tableBody.innerHTML = ''; // Limpiar el contenido previo de la tabla

    orders.forEach(order => {
        const row = document.createElement('tr');

        // Mostrar el ID del usuario y su nombre
        const userInfo = order.userID 
            ? `${order.userID.userID} - ${order.userID.name}`
            : "Sin información del usuario";

        // Convertir y mostrar la fecha de creación
        const createdAt = order.createdAt
            ? new Date(order.createdAt).toLocaleString()
            : 'N/A';

        // Construir la fila de la tabla
        row.innerHTML = `
            <td>${order.orderID || 'N/A'}</td>
            <td>${userInfo}</td>
            <td>${createdAt}</td>
            <td>${order.status ? 'Activo' : 'Inactivo'}</td>
            <td>${order.totalPrice ? `$${order.totalPrice.toFixed(2)}` : 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-primary me-1" onclick="editOrder(${order.orderID})" title="Editar">
                    <i class="fa fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger me-1" onclick="deleteOrder(${order.orderID})" title="Eliminar">
                    <i class="fa fa-trash"></i>
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

// Función para cargar usuarios en el menú desplegable
async function loadUsersForMenus() {
    try {
        console.log("Cargando usuarios desde la API..."); // Mensaje de depuración
        const response = await fetch(apiUrlUsers);

        if (!response.ok) {
            throw new Error("Error al obtener los usuarios.");
        }

        const allUsers = await response.json(); // Obtiene los usuarios de la API
        console.log("Usuarios obtenidos:", allUsers); // Depuración

        // Llenar ambos selects de usuarios
        const userFilter = document.getElementById('userFilter'); // Filtro de usuarios
        const userName = document.getElementById('userName'); // Menú para insertar órdenes

        // Limpiar y agregar opciones a ambos menús desplegables
        const menus = [userFilter, userName]; // Lista de menús desplegables a actualizar

        menus.forEach(menu => {
            if (menu) {
                menu.innerHTML = '<option value="">Seleccione un usuario</option>'; // Opción por defecto

                allUsers.forEach(user => {
                    const option = document.createElement('option');
                    option.value = user.userID; // Usar el ID del usuario como valor
                    option.textContent = user.name; // Mostrar el nombre del usuario
                    menu.appendChild(option); // Agregar la opción al menú desplegable
                });

                console.log(`Menú desplegable con ID '${menu.id}' actualizado con éxito.`);
            } else {
                console.warn(`No se encontró el menú desplegable con el ID correspondiente.`);
            }
        });
    } catch (error) {
        console.error("Error al cargar los usuarios:", error);
        alert("No se pudieron cargar los usuarios. Intente nuevamente más tarde.");
    }
}

// Función para aplicar los filtros de estado y usuario
function applyFilters() {
    const selectedStatus = document.getElementById('statusFilter').value.toLowerCase(); // Estado seleccionado ("activo", "inactivo" o "")
    const selectedUserID = document.getElementById('userFilter').value; // Usuario seleccionado (ID o vacío)

    const filteredOrders = allOrders.filter(order => {
        // Coincidencia de estado
        const statusMatch =
            selectedStatus === "" || // Si "Todos los estados" está seleccionado
            (selectedStatus === "activo" && order.status === true) ||
            (selectedStatus === "inactivo" && order.status === false);

        // Coincidencia de usuario
        const userMatch =
            selectedUserID === "" || // Si "Todos los usuarios" está seleccionado
            (order.userID && order.userID.userID.toString() === selectedUserID);

        return statusMatch && userMatch; // Ambas condiciones deben coincidir
    });

    displayOrders(filteredOrders); // Mostrar las órdenes filtradas
}

// Inicializar la página cargando los usuarios y las órdenes
document.addEventListener('DOMContentLoaded', function () {
    loadUsersForMenus(); // Cargar usuarios en los menús desplegables
    loadOrders(); // Cargar las órdenes iniciales

    // Vincular eventos de cambio a los menús desplegables para aplicar filtros dinámicamente
    document.getElementById('statusFilter').addEventListener('change', applyFilters);
    document.getElementById('userFilter').addEventListener('change', applyFilters);
});
// Inicializar la página cargando los usuarios y las órdenes
document.addEventListener('DOMContentLoaded', function () {
    loadUsersForMenus(); // Cargar usuarios en los menús desplegables
    loadOrders(); // Cargar las órdenes iniciales

    // Vincular eventos de cambio a los menús desplegables para aplicar filtros dinámicamente
    document.getElementById('statusFilter').addEventListener('change', applyFilters);
    document.getElementById('userFilter').addEventListener('change', applyFilters);
});
    // Función para agregar una nueva orden
    document.getElementById('addOrderForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const userID = document.getElementById('userName').value;

        // Validar que se haya seleccionado un usuario
        if (!userID) {
            alert("Por favor seleccione un usuario.");
            return;
        }

        const orderData = { userID };

        try {
            const response = await fetch(apiUrlOrders, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });

            if (response.ok) {
                alert("Orden creada correctamente.");
                document.getElementById('addOrderForm').reset(); // Limpiar el formulario
                loadOrders(); // Recargar la lista de órdenes
                closeModal(); // Cerrar el modal
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al crear la orden.");
            }
        } catch (error) {
            console.error("Error al crear la orden:", error);
            alert("No se pudo crear la orden. Intente nuevamente más tarde.");
        }
    });

    function closeModal() {
        const modalElement = document.getElementById('addOrderModal'); // ID del modal
        const modalInstance = bootstrap.Modal.getInstance(modalElement); // Obtén la instancia del modal
        modalInstance.hide(); // Cierra el modal
    }

    
    async function loadUsersForEditMenu() {
        try {
            const response = await fetch(apiUrlUsers);
    
            if (!response.ok) {
                throw new Error("Error al obtener los usuarios.");
            }
    
            const allUsers = await response.json();
    
            const editOrderUser = document.getElementById('editOrderUser');
            if (editOrderUser) {
                editOrderUser.innerHTML = '<option value="">Seleccione un usuario</option>'; // Opción por defecto
    
                allUsers.forEach(user => {
                    const option = document.createElement('option');
                    option.value = user.userID; // Usar el ID del usuario como valor
                    option.textContent = user.name; // Mostrar el nombre del usuario
                    editOrderUser.appendChild(option);
                });
    
                console.log("Menú desplegable de usuarios actualizado con éxito.");
            }
        } catch (error) {
            console.error("Error al cargar los usuarios en el formulario de edición:", error);
        }
    }

    // Función para manejar la edición de una orden
    window.editOrder = async function (orderID) {
        try {
            const response = await fetch(`${apiUrlOrders}${orderID}`);
    
            if (!response.ok) {
                throw new Error("Error al obtener los datos de la orden.");
            }
    
            const order = await response.json();
    
            console.log("Datos de la orden:", order); // Depuración para verificar la respuesta
    
            // Llenar el formulario de edición con los datos de la orden
            document.getElementById('editOrderID').value = order.orderID;
    
            // Llenar el menú desplegable de usuarios
            await loadUsersForEditMenu();
    
            if (!order.user) {
                document.getElementById('editOrderUser').value = ""; // Dejar vacío si no hay usuario
                // Muestra un mensaje dentro del modal en lugar de usar alert()
                const userWarning = document.getElementById('userWarningMessage');
                if (userWarning) {
                    userWarning.textContent = "La orden no tiene un usuario asociado. Seleccione un usuario para asignarlo.";
                    userWarning.style.display = "block"; // Mostrar el mensaje
                }
            } else {
                document.getElementById('editOrderUser').value = order.user.userID;
                const userWarning = document.getElementById('userWarningMessage');
                if (userWarning) {
                    userWarning.style.display = "none"; // Ocultar el mensaje si no es necesario
                }
            }
    
            // Mostrar el modal de edición
            const editOrderModal = new bootstrap.Modal(document.getElementById('editOrderModal'));
            editOrderModal.show();
        } catch (error) {
            console.error("Error al cargar los datos de la orden:", error);
            alert(error.message || "No se pudieron cargar los datos de la orden.");
        }
    };


// Función para manejar el envío del formulario de edición
document.getElementById('editOrderForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const orderID = document.getElementById('editOrderID').value;
    const updatedUserID = document.getElementById('editOrderUser').value;

    try {
        const response = await fetch(`${apiUrlOrders}${orderID}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userID: updatedUserID, // Envía el nuevo userID
            }),
        });

        if (!response.ok) {
            throw new Error("Error al guardar los cambios de la orden.");
        }

        // Ocultar el modal de edición
        const editOrderModal = bootstrap.Modal.getInstance(document.getElementById('editOrderModal'));
        editOrderModal.hide();

        // Recargar las órdenes
        alert("Orden actualizada con éxito.");
        await loadOrders();
    } catch (error) {
        console.error("Error al guardar los cambios de la orden:", error);
        alert("No se pudieron guardar los cambios de la orden.");
    }
});

    // Funciones adicionales: eliminar y reactivar órdenes
    window.deleteOrder = async function (orderID) {
        const confirmDelete = confirm("¿Estás seguro de que deseas eliminar esta orden?");
        if (!confirmDelete) return;

        try {
            const response = await fetch(`${apiUrlOrders}${orderID}`, {
                method: "DELETE"
            });

            if (response.ok) {
                alert("Orden eliminada correctamente.");
                loadOrders();
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al eliminar la orden.");
            }
        } catch (error) {
            console.error("Error al eliminar la orden:", error);
            alert("No se pudo eliminar la orden. Intente nuevamente más tarde.");
        }
    };

    window.reactivateOrder = async function (orderID) {
        const confirmAction = confirm("¿Estás seguro de que deseas reactivar esta orden?");
        if (!confirmAction) return;

        try {
            const response = await fetch(`${apiUrlOrders}${orderID}/reactivate`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" }
            });

            if (response.ok) {
                alert("Orden reactivada correctamente.");
                loadOrders();
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Error al reactivar la orden.");
            }
        } catch (error) {
            console.error("Error al reactivar la orden:", error);
            alert("No se pudo reactivar la orden. Intente nuevamente más tarde.");
        }
    };

    // Escuchar eventos de búsqueda y filtros
    userFilter.addEventListener('change', applyFilters); // Filtrar por usuario seleccionado
statusFilter.addEventListener('change', applyFilters); // Filtrar por estado seleccionado

    // Cargar usuarios y órdenes al cargar la página
    loadUsersForMenus();
    loadOrders();
});