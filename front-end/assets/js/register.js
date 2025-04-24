document.addEventListener('DOMContentLoaded', function () {
    // Función para obtener los roles
    async function obtenerRoles() {
        const selectRoles = document.getElementById("opciones-rol");
        
        // Validar si el select existe
        if (!selectRoles) {
            console.error("El elemento select para roles no existe.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/v1/role/");
            
            if (!response.ok) {
                throw new Error("Error al obtener los roles.");
            }

            const roles = await response.json();
            
            // Limpiar el select antes de llenarlo
            selectRoles.innerHTML = '<option value="">Seleccione un rol</option>';
            
            // Agregar las opciones al select
            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role.roleID; // Usar el ID del rol como valor
                option.textContent = role.name; // Mostrar el nombre del rol
                selectRoles.appendChild(option);
            });
        } catch (error) {
            console.error("Error al cargar los roles:", error);
            alert("No se pudieron cargar los roles. Intente nuevamente más tarde.");
        }
    }

    // Llamar a la función para obtener los roles al cargar la página
    obtenerRoles();

    document.getElementById('registerForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        
        // Obtener valores del formulario
        const name = document.getElementById('registerName').value.trim();
        const email = document.getElementById('registerEmail').value.trim();
        const password = document.getElementById('registerPassword').value;
        const role = document.getElementById('opciones-rol').value;
    
        // Validaciones
        if (!name || !email || !password || role === "") {
            alert("Por favor, complete todos los campos.");
            return;
        }
    
        if (!/\S+@\S+\.\S+/.test(email)) {
            alert("Por favor, ingrese un correo electrónico válido.");
            return;
        }
    
        const headersList = {
            "Accept": "*/*",
            "User-Agent": "web",
            "Content-Type": "application/json"
        };
        
        const bodyContent = JSON.stringify({
            "name": name,
            "email": email,
            "password": password,
            "roleID": role
        });
        
        try {
            const response = await fetch("http://localhost:8080/api/v1/user/register", {
                method: "POST",
                body: bodyContent,
                headers: headersList
            });
    
            const data = await response.json();
    
            // Verificar primero el status en la respuesta
            if (data.status === "error") {
                alert(data.message || "Error al registrar el usuario.");
                return;
            }
    
            // Si llegamos aquí, el registro fue exitoso
            console.log("Registro exitoso. Datos:", data);
            
            // Obtener el roleID de la respuesta
            const roleID = data.roleID;
            console.log("RoleID recibido:", roleID);
            
            // Almacenar en localStorage si es necesario
            localStorage.setItem('userRoleID', roleID);
            
            // Mostrar mensaje de éxito
            alert("Usuario registrado correctamente. Serás redirigido al inicio.");
            window.location.href = 'index.html';
    
        } catch (error) {
            console.error("Error en la petición:", error);
            alert("Error de conexión al registrar el usuario: " + error.message);
        }
    });
});