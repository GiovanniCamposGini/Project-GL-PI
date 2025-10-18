document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Você precisa estar logado.");
        window.location.href = "login.html";
        return;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    const userId = payload.id;
    const isAdmin = payload.groups === "ADMIN";

    // Buscar dados do usuário logado
    fetch(`http://localhost:3000/users/${userId}`, {
        headers: {
            Authorization: "Bearer " + token
        }
    })
        .then(res => res.json())
        .then(user => {
            const area = document.getElementById("userArea");
            let botoes = `
            <button onclick="editarUsuario()" style="background:#f39c12;">Editar</button>
`;

            if (isAdmin) {
                botoes += `
    <button onclick="atualizarStatusUsuario()" style="background:#2980b9;">Atualizar Status</button>
    <button onclick="abrirFormularioCadastro()" style="background:#27ae60;">Adicionar Usuário</button>
    <button onclick="window.location.href='usuarios.html'" style="background:#2ecc71;">Mostrar Usuários
  `;
            }

            area.innerHTML = `
  <p><strong>CPF:</strong> ${user.cpf}</p>
  <p><strong>Nome:</strong> ${user.name}</p>
  <p><strong>Email:</strong> ${user.email}</p>
  <p><strong>Cargo:</strong> ${user.groups}</p>
  <p><strong>Status Banco:</strong> ${user.statusBanco}</p>
  ${botoes}
`
        });

    // Se for admin, buscar todos os usuários
    if (isAdmin) {
        fetch("http://localhost:3000/users", {
            headers: {
                Authorization: "Bearer " + token
            }
        })
            .then(res => res.json())
            .then(users => {
                const listArea = document.getElementById("userListArea");
                listArea.innerHTML = "<h3>Todos os usuários</h3><ul>" + users.map(u =>
                    `<li>ID: ${u.id} | ${u.name} (${u.email}) - ${u.groups} - Status: ${u.statusBanco}</li>`
                ).join("") + "</ul>";
            });
    }
});

function abrirFormularioCadastro() {
    const area = document.getElementById("formularioCadastroArea");
    area.innerHTML = `
    <h3>Cadastro de Novo Usuário</h3>
    <input type="text" id="novoNome" placeholder="Nome"><br>
    <input type="email" id="novoEmail" placeholder="Email"><br>
    <input type="password" id="novaSenha" placeholder="Senha"><br>
    <input type="text" id="novoCpf" placeholder="CPF"><br>
    <select id="novoCargo">
      <option value="USER">USER</option>
      <option value="ADMIN">ADMIN</option>
    </select><br>
    <select id="novoStatus">
      <option value="ATIVO">ATIVO</option>
      <option value="INATIVO">INATIVO</option>
    </select><br>
    <button onclick="cadastrarNovoUsuario()" style="background:#2ecc71;">Cadastrar</button>
  `;
}

function cadastrarNovoUsuario() {
    const token = localStorage.getItem("token");

    const nome = document.getElementById("novoNome").value;
    const email = document.getElementById("novoEmail").value;
    const senha = document.getElementById("novaSenha").value;
    const cpf = document.getElementById("novoCpf").value;
    const cargo = document.getElementById("novoCargo").value;
    const status = document.getElementById("novoStatus").value;

    if (!nome || !email || !senha || !cpf || !cargo || !status) {
        alert("Preencha todos os campos.");
        return;
    }

    fetch("http://localhost:3000/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify({
            name: nome,
            email: email,
            password: senha,
            cpf: cpf,
            groups: cargo,
            statusBanco: status
        })
    })
        .then(res => {
            if (res.ok) {
                alert("Usuário cadastrado com sucesso!");
                document.getElementById("formularioCadastroArea").innerHTML = "";
                location.reload(); // ou chamar renderListaUsuarios() se quiser evitar reload
            } else {
                res.text().then(text => alert("Erro: " + text));
            }
        })
        .catch(err => {
            console.error("Erro ao cadastrar usuário:", err);
            alert("Falha no cadastro.");
        });
}

function editarUsuario() {
    const token = localStorage.getItem("token");
    const payload = JSON.parse(atob(token.split('.')[1]));
    const isAdmin = payload.groups === "ADMIN";

    const area = document.getElementById("formularioEdicaoArea");

    area.innerHTML = `
    <h3>Editar Usuário</h3>
    ${isAdmin ? `
      <input type="number" id="editId" placeholder="ID do usuário"><br>
    ` : ""}
    <input type="text" id="editNome" placeholder="Novo nome"><br>
    <input type="email" id="editEmail" placeholder="Novo email"><br>
    <input type="password" id="editSenha" placeholder="Nova senha"><br>
    ${isAdmin ? `
      <select id="editCargo">
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select><br>
    ` : ""}
    <button onclick="salvarEdicaoUsuario()" style="background:#3498db;">Salvar</button>
  `;
}

function salvarEdicaoUsuario() {
    const token = localStorage.getItem("token");
    const payload = JSON.parse(atob(token.split('.')[1]));
    const isAdmin = payload.groups === "ADMIN";

    // Captura o ID do usuário
    const id = isAdmin
        ? document.getElementById("editId").value
        : payload.id;

    if (!id || isNaN(parseInt(id))) {
        alert("ID inválido.");
        return;
    }

    // Captura os dados do formulário
    const nome = document.getElementById("editNome").value;
    const email = document.getElementById("editEmail").value;
    const senha = document.getElementById("editSenha").value;

    if (!nome || !email || !senha) {
        alert("Preencha todos os campos.");
        return;
    }

    // Monta o corpo da requisição
    const body = {
        name: nome,
        email: email,
        password: senha
    };

    if (!isAdmin) {
        body.groups = "USER";
    }

// Se for admin e o campo estiver presente, usa o valor selecionado
    const cargoEl = document.getElementById("editCargo");
    if (isAdmin && cargoEl && cargoEl.value !== "") {
        body.groups = cargoEl.value;
    }

    // Envia a requisição
    fetch(`http://localhost:3000/users/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify(body)
    })
        .then(res => {
            if (res.ok) {
                alert("Usuário atualizado com sucesso!");
                document.getElementById("formularioEdicaoArea").innerHTML = "";
                location.reload();
            } else {
                res.text().then(text => alert("Erro: " + text));
            }
        })
        .catch(err => {
            console.error("Erro ao editar usuário:", err);
            alert("Falha na atualização.");
        });
}

function atualizarStatusUsuario() {
    const token = localStorage.getItem("token");
    const area = document.getElementById("formularioStatusArea");

    area.innerHTML = `
    <h3>Alterar Status de Usuário</h3>
    <input type="number" id="statusId" placeholder="ID do usuário"><br>
    <select id="novoStatusBanco">
      <option value="ATIVO">ATIVO</option>
      <option value="INATIVO">INATIVO</option>
    </select><br>
    <button onclick="salvarNovoStatus()" style="background:#8e44ad;">Salvar Status</button>
  `;
}

function salvarNovoStatus() {
    const token = localStorage.getItem("token");
    const id = document.getElementById("statusId").value;
    const novoStatus = document.getElementById("novoStatusBanco").value;

    if (!id || isNaN(parseInt(id))) {
        alert("ID inválido.");
        return;
    }

    fetch(`http://localhost:3000/users/${id}/status`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify({ statusBanco: novoStatus })
    })
        .then(res => {
            if (res.ok) {
                alert(`Status atualizado para ${novoStatus}!`);
                document.getElementById("formularioStatusArea").innerHTML = "";
                location.reload();
            } else {
                res.text().then(text => alert("Erro: " + text));
            }
        })
        .catch(err => {
            console.error("Erro ao atualizar status:", err);
            alert("Falha na atualização.");
        });
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

