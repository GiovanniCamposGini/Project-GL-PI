function carregarUsuarios() {
    const token = localStorage.getItem("token");

    fetch("http://localhost:3000/users", {
        method: "GET",
        headers: {
            Authorization: "Bearer " + token
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Erro ao buscar usuários");
            return res.json();
        })
        .then(usuarios => {
            const container = document.getElementById("usuariosContainer");
            container.innerHTML = "";

            usuarios.forEach(user => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
          <h3>${user.name}</h3>
          <p><strong>Email:</strong> ${user.email}</p>
          <p><strong>Status:</strong> ${user.statusBanco}</p>
          <p><strong>Cargo:</strong> ${user.groups}</p>
          <p><strong>ID:</strong> ${user.id}</p>
        `;
                container.appendChild(card);
            });
        })
        .catch(err => {
            console.error("Erro ao carregar usuários:", err);
            alert("Não foi possível carregar os usuários.");
        });
}

function voltarDashboard() {
    window.location.href = "dashboard.html";
}

window.addEventListener("DOMContentLoaded", carregarUsuarios);