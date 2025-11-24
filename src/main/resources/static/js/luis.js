
document.addEventListener("DOMContentLoaded", () => {
    initSampleLoja();
    setupLojaPages();
    updateUserUI();
    updateCartCountUI();
    userVerification();
});


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

    updateCartCountUI();
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

function userVerification() {
    const usuario = JSON.parse(localStorage.getItem("logado"));
    const adminLink = document.getElementById("adminLink");

    if (!usuario && adminLink) {
        adminLink.addEventListener("click", (e) => {
            e.preventDefault();
            alert("Você precisa estar logado para acessar essa área.");
        });
    }
}

