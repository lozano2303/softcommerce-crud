document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("loginForm").addEventListener("submit", async function (e) {
        e.preventDefault();

        // Obtener valores del formulario
        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        // Validaciones en el frontend
        if (!email || !password) {
            alert("Por favor, complete todos los campos.");
            return;
        }

        if (!/\S+@\S+\.\S+/.test(email)) {
            alert("Por favor, ingrese un correo electrónico válido.");
            return;
        }

        const headersList = {
            Accept: "application/json",
            "Content-Type": "application/json",
        };

        const bodyContent = JSON.stringify({
            email: email,
            password: password,
        });

        try {
            const response = await fetch("http://localhost:8080/api/v1/user/login", {
                method: "POST",
                body: bodyContent,
                headers: headersList,
            });

            // Mostrar logs para depurar
            console.log("Estado de respuesta:", response.status);

            const data = await response.json();

            // Verificar la respuesta del backend
            if (!response.ok || data.status === "error") {
                alert(data.message || "Error al iniciar sesión.");
                return;
            }

            // Si el inicio de sesión es exitoso
            console.log("Inicio de sesión exitoso. Datos:", data);

            // Guardar token en localStorage
            if (data.token) {
                localStorage.setItem("authToken", data.token);
            }

            // Redirigir al dashboard
            alert("Inicio de sesión exitoso. Serás redirigido al panel.");
            window.location.href = "dashboard.html";
        } catch (error) {
            console.error("Error en la petición:", error);
            alert("Error de conexión: " + (error.message || "Intente nuevamente más tarde."));
        }
    });
});