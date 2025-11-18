
document.addEventListener("DOMContentLoaded", () => {
    initSampleLoja();
    setupAuthForms();
    setupEstoquePage();
    setupLojaPages();
    setupHistoriaPage();
    updateUserUI();
    updateCartCountUI();
});

/* ---------------------------
   Autenticação: cadastro / login
   --------------------------- */
function setupAuthForms() {
    // CADASTRO
    const cadastroForm = document.getElementById("cadastroForm");
    if (cadastroForm) {
        cadastroForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const cpf = document.getElementById("cpf").value.trim();
            const nome = document.getElementById("nome").value.trim();
            const nascimento = document.getElementById("nascimento").value;
            const senha = document.getElementById("senha").value;
            const confirma = document.getElementById("confirma").value;
            const cargo = document.getElementById("cargo").value;

            if (senha !== confirma) {
                alert("As senhas não coincidem!");
                return;
            }

            let usuarios = JSON.parse(localStorage.getItem("usuarios")) || [];
            if (usuarios.some(u => u.cpf === cpf)) {
                alert("CPF já cadastrado!");
                return;
            }

            usuarios.push({ cpf, nome, nascimento, senha, cargo });
            localStorage.setItem("usuarios", JSON.stringify(usuarios));
            alert("Cadastro realizado com sucesso!");
            window.location.href = "login.html";
        });
    }

    // LOGIN
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const cpf = document.getElementById("loginCpf").value.trim();
            const senha = document.getElementById("loginSenha").value;

            let usuarios = JSON.parse(localStorage.getItem("usuarios")) || [];
            let usuario = usuarios.find(u => u.cpf === cpf && u.senha === senha);

            if (!usuario) {
                alert("CPF ou senha incorretos!");
                return;
            }

            localStorage.setItem("logado", JSON.stringify(usuario));
            updateUserUI();
            window.location.href = "estoque.html";
        });
    }
}

/* ---------------------------
   Estoque
   --------------------------- */
function setupEstoquePage() {
    if (window.location.pathname.includes("estoque.html")) {
        let usuario = JSON.parse(localStorage.getItem("logado"));
        if (!usuario) {
            window.location.href = "login.html";
            return;
        }

        const title = document.querySelector("h2");
        if (title) title.innerText += " - " + usuario.nome;

        // renderizar produtos do estoque (usado para admin)
        let produtos = JSON.parse(localStorage.getItem("produtos")) || [];
        renderEstoque(produtos, usuario);

        if (usuario.cargo === "adm") {
            const admArea = document.getElementById("admArea");
            if (admArea) admArea.style.display = "block";
        }
    }
}

function renderEstoque(produtos, usuario) {
    const area = document.getElementById("estoqueArea");
    if (!area) return;

    let html = "<ul>";
    produtos.forEach((p, i) => {
        html += `<li>${p.nome} - ${p.qtd}`;
        if (usuario && usuario.cargo === "adm") {
            html += ` 
        <button onclick="editarProduto(${i})" style="margin-left:5px; background:#f39c12;">Editar</button>
        <button onclick="excluirProduto(${i})" style="margin-left:5px; background:#e74c3c;">Excluir</button>`;
        }
        html += "</li>";
    });
    html += "</ul>";
    area.innerHTML = html;
}

window.editarProduto = function(index) {
    let produtos = JSON.parse(localStorage.getItem("produtos")) || [];
    let produto = produtos[index];

    let novoNome = prompt("Novo nome do produto:", produto.nome);
    let novaQtd = prompt("Nova quantidade:", produto.qtd);

    if (novoNome && novaQtd) {
        produtos[index] = { nome: novoNome, qtd: novaQtd };
        localStorage.setItem("produtos", JSON.stringify(produtos));
        let usuario = JSON.parse(localStorage.getItem("logado"));
        renderEstoque(produtos, usuario);
    }
};

window.excluirProduto = function(index) {
    let produtos = JSON.parse(localStorage.getItem("produtos")) || [];
    if (confirm("Tem certeza que deseja excluir este produto?")) {
        produtos.splice(index, 1);
        localStorage.setItem("produtos", JSON.stringify(produtos));
        let usuario = JSON.parse(localStorage.getItem("logado"));
        renderEstoque(produtos, usuario);
    }
};

window.logout = function() {
    localStorage.removeItem("logado");
    updateUserUI();
    window.location.href = "login.html";
};

/* ---------------------------
   LOJA: produtos, carrinho, checkout
   --------------------------- */

function initSampleLoja() {
    // Se não existe lista de produtos da loja, cria alguns exemplos
    let loja = JSON.parse(localStorage.getItem("produtosLoja")) || null;
    if (!loja) {
        const sample = [
            { id: 1, nome: "Chinelo Classic", preco: 39.90, imagem: "https://picsum.photos/seed/c1/600/400", estoque: 20 },
            { id: 2, nome: "Chinelo Confort", preco: 49.90, imagem: "https://picsum.photos/seed/c2/600/400", estoque: 15 },
            { id: 3, nome: "Chinelo Sport", preco: 59.90, imagem: "https://picsum.photos/seed/c3/600/400", estoque: 10 },
            { id: 4, nome: "Chinelo Premium", preco: 79.90, imagem: "https://picsum.photos/seed/c4/600/400", estoque: 5 }
        ];
        localStorage.setItem("produtosLoja", JSON.stringify(sample));
    }
}

function setupLojaPages() {
    if (window.location.pathname.includes("index.html") || window.location.pathname === "/" ) {
        renderLojaProdutos();
    }

    if (window.location.pathname.includes("carrinho.html")) {
        renderCarrinho();
    }
}

function renderLojaProdutos() {
    const area = document.getElementById("produtosGrid");
    if (!area) return;

    const produtos = JSON.parse(localStorage.getItem("produtosLoja")) || [];
    let html = "";
    produtos.forEach(p => {
        html += `
      <div class="card">
        <img src="${p.imagem}" alt="${escapeHtml(p.nome)}" />
        <h4>${escapeHtml(p.nome)}</h4>
        <p class="price">R$ ${formatPrice(p.preco)}</p>
        <p>Estoque: ${p.estoque}</p>
        <div>
          <button class="primary" onclick="addToCart(${p.id})">Adicionar ao carrinho</button>
        </div>
      </div>
    `;
    });
    area.innerHTML = html;
}

window.addToCart = function(productId) {
    const usuario = JSON.parse(localStorage.getItem("logado"));
    if (!usuario) {
        if (confirm("Você precisa estar logado para comprar. Deseja entrar agora?")) {
            window.location.href = "login.html";
        }
        return;
    }

    const produtos = JSON.parse(localStorage.getItem("produtosLoja")) || [];
    const produto = produtos.find(p => p.id === productId);
    if (!produto) {
        alert("Produto não encontrado.");
        return;
    }
    if (produto.estoque <= 0) {
        alert("Produto sem estoque.");
        return;
    }

    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    // se já existe, aumenta quantidade
    let item = cart.find(i => i.id === productId);
    if (item) {
        if (item.qtd + 1 > produto.estoque) {
            alert("Quantidade excede estoque disponível.");
            return;
        }
        item.qtd += 1;
    } else {
        cart.push({ id: produto.id, nome: produto.nome, preco: produto.preco, imagem: produto.imagem, qtd: 1 });
    }

    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartCountUI();
    alert("Produto adicionado ao carrinho!");
};

function updateCartCountUI() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const count = cart.reduce((s,i) => s + i.qtd, 0);
    const el = document.getElementById("cartCount");
    if (el) el.innerText = count;
}

/* ---------------------------
   Carrinho: render, alterar qtd, remover, checkout
   --------------------------- */
function renderCarrinho() {
    const area = document.getElementById("cartArea");
    const actions = document.getElementById("cartActions");
    if (!area) return;

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    if (cart.length === 0) {
        area.innerHTML = "<p>Seu carrinho está vazio.</p>";
        actions.innerHTML = `<button class="ghost" onclick="window.location.href='index.html'">Continuar comprando</button>`;
        updateCartCountUI();
        return;
    }

    let html = "";
    cart.forEach((item, idx) => {
        html += `
      <div class="cart-item">
        <img src="${item.imagem}" alt="${escapeHtml(item.nome)}" />
        <div class="meta">
          <strong>${escapeHtml(item.nome)}</strong>
          <p>R$ ${formatPrice(item.preco)} cada</p>
        </div>
        <div class="qty-controls">
          <label>Qtd</label><br/>
          <input type="number" min="1" value="${item.qtd}" onchange="changeQty(${idx}, this.value)" />
        </div>
        <div>
          <p><strong>R$ ${formatPrice(item.preco * item.qtd)}</strong></p>
          <button onclick="removeFromCart(${idx})" class="ghost">Remover</button>
        </div>
      </div>
    `;
    });

    // resumo
    const total = cart.reduce((s,i) => s + i.preco * i.qtd, 0);
    html += `<div style="margin-top:12px;"><strong>Total: R$ ${formatPrice(total)}</strong></div>`;

    area.innerHTML = html;

    actions.innerHTML = `
    <button class="primary" onclick="checkout()">Finalizar Compra</button>
    <button class="ghost" onclick="clearCart()">Limpar carrinho</button>
  `;

    updateCartCountUI();
}

window.changeQty = function(index, novo) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    novo = parseInt(novo);
    if (!novo || novo < 1) novo = 1;

    // verifica estoque disponível
    const produtos = JSON.parse(localStorage.getItem("produtosLoja")) || [];
    const produto = produtos.find(p => p.id === cart[index].id);
    if (produto && novo > produto.estoque) {
        alert("Quantidade maior que o estoque disponível.");
        renderCarrinho();
        return;
    }

    cart[index].qtd = novo;
    localStorage.setItem("cart", JSON.stringify(cart));
    renderCarrinho();
};

window.removeFromCart = function(index) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart.splice(index, 1);
    localStorage.setItem("cart", JSON.stringify(cart));
    renderCarrinho();
};

window.clearCart = function() {
    if (confirm("Deseja limpar o carrinho?")) {
        localStorage.removeItem("cart");
        renderCarrinho();
    }
};

window.checkout = function() {
    const usuario = JSON.parse(localStorage.getItem("logado"));
    if (!usuario) {
        alert("Você precisa estar logado para finalizar a compra.");
        window.location.href = "login.html";
        return;
    }

    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    if (cart.length === 0) {
        alert("Carrinho vazio.");
        return;
    }

    // Simula redução de estoque
    let produtos = JSON.parse(localStorage.getItem("produtosLoja")) || [];
    for (let item of cart) {
        const prod = produtos.find(p => p.id === item.id);
        if (prod) {
            if (item.qtd > prod.estoque) {
                alert(`Produto ${prod.nome} não tem estoque suficiente.`);
                return;
            }
            prod.estoque -= item.qtd;
        }
    }
    localStorage.setItem("produtosLoja", JSON.stringify(produtos));
    // opcional: adicionar à tabela de vendas (não exigido)
    localStorage.removeItem("cart");
    alert("Compra finalizada com sucesso! Obrigado pela sua compra.");
    updateCartCountUI();
    // redireciona para página de produtos
    window.location.href = "index.html";
};

/* ---------------------------
   História: upload e apresentação de fotos
   --------------------------- */
function setupHistoriaPage() {
    if (!window.location.pathname.includes("historia.html")) return;

    const input = document.getElementById("photoInput");
    const saveBtn = document.getElementById("savePhotosBtn");
    const grid = document.getElementById("photosGrid");

    function renderPhotos() {
        const fotos = JSON.parse(localStorage.getItem("historiaFotos")) || [];
        grid.innerHTML = fotos.map((f, i) => `<img src="${f}" alt="Equipe ${i+1}">`).join("");
    }

    renderPhotos();

    if (input && saveBtn) {
        saveBtn.addEventListener("click", () => {
            const files = input.files;
            if (!files || files.length === 0) {
                alert("Escolha ao menos uma imagem.");
                return;
            }
            const promises = [];
            for (let f of files) {
                promises.push(fileToDataURL(f));
            }
            Promise.all(promises).then(dataUrls => {
                // salva no localStorage
                localStorage.setItem("historiaFotos", JSON.stringify(dataUrls));
                renderPhotos();
                alert("Fotos salvas localmente e exibidas na história.");
            });
        });
    }
}

function fileToDataURL(file) {
    return new Promise((resolve, reject) => {
        const fr = new FileReader();
        fr.onload = () => resolve(fr.result);
        fr.onerror = reject;
        fr.readAsDataURL(file);
    });
}

/* ---------------------------
   UI: área do usuário (header)
   --------------------------- */
function updateUserUI() {
    const userArea = document.getElementById("userArea");
    const usuario = JSON.parse(localStorage.getItem("logado"));
    if (!userArea) return;
    if (usuario) {
        userArea.innerHTML = `<span style="font-weight:700;">${usuario.nome}</span> • <a href="#" onclick="logout()" style="color:#ffd700; margin-left:8px;">Sair</a>`;
    } else {
        userArea.innerHTML = `<a href="login.html">Entrar</a> • <a href="cadastro.html" style="margin-left:6px;">Cadastrar</a>`;
    }
}

/* ---------------------------
   Helpers
   --------------------------- */
function formatPrice(v) {
    // garante duas casas decimais com vírgula
    return Number(v).toFixed(2).replace('.', ',');
}
function escapeHtml(text) {
    if (!text) return "";
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

/* ---------------------------
   Mantém compatibilidade com suas funções originais (se for usado em outros arquivos)
   --------------------------- */
function adicionarProduto() {
    const nome = document.getElementById("produtoNome").value;
    const qtd = document.getElementById("produtoQtd").value;

    if (!nome || !qtd) {
        alert("Preencha todos os campos!");
        return;
    }

    let produtos = JSON.parse(localStorage.getItem("produtos")) || [];
    produtos.push({ nome, qtd });
    localStorage.setItem("produtos", JSON.stringify(produtos));

    let usuario = JSON.parse(localStorage.getItem("logado"));
    renderEstoque(produtos, usuario);

    document.getElementById("produtoNome").value = "";
    document.getElementById("produtoQtd").value = "";
}

/* Quando o usuário carregar páginas sem extensão (ex: /) redirecionamos para index.html */
if (window.location.pathname === "/" || window.location.pathname === "") {
    // se for app em root, redireciona para index.html
    // não forçamos se estiver hospedando em localhost com arquivo aberto diretamente.
}
