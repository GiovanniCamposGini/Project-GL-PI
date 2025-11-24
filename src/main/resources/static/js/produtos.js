async function carregarProdutos() {
    const token = localStorage.getItem("token");

    try {
        const resposta = await fetch("http://localhost:3000/products");

        if (!resposta.ok) throw new Error("Erro ao buscar produtos");

        const produtos = await resposta.json();
        const grid = document.getElementById("produtosGrid");
        grid.innerHTML = "";

        produtos.forEach(produto => {
            const card = document.createElement("div");
            card.className = "produto-card";
            card.innerHTML = `
                  <img src="${produto.imgURL}" alt="${produto.name}" />
                  <h3>${produto.name}</h3>
                  <div class="info-area">
                    <p>${produto.description}</p>
                    <p><strong>Preço:</strong> R$ ${produto.price}</p>
                    <input type="number" min="1" value="1" id="qtd-${produto.id}" />
                    <button onclick="adicionarAoCarrinho(${produto.id})">Adicionar ao Carrinho</button>
                  </div>`;
            grid.appendChild(card);
        });
    } catch (erro) {
        console.error("Erro ao carregar produtos:", erro);
        alert("Não foi possível carregar os produtos.");
    }
}

async function adicionarAoCarrinho(productId) {
    const token = localStorage.getItem("token");
    const quantidade = document.getElementById(`qtd-${productId}`).value;

    try {
        const resposta = await fetch("http://localhost:3000/carrinho/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                productId: productId,
                quantity: parseInt(quantidade)
            })
        });

        if (!resposta.ok) throw new Error("Erro ao adicionar ao carrinho");

        alert("Produto adicionado ao carrinho!");
        atualizarContadorCarrinho();
    } catch (erro) {
        console.error("Erro ao adicionar ao carrinho:", erro);
        alert("Não foi possível adicionar ao carrinho.");
    }
}

async function atualizarContadorCarrinho() {
    const token = localStorage.getItem("token");

    try {
        const resposta = await fetch("http://localhost:3000/carrinho", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!resposta.ok) return;

        const carrinho = await resposta.json();
        const totalItens = carrinho.items.reduce((acc, item) => acc + item.quantity, 0);
        document.getElementById("cartCount").textContent = totalItens;
    } catch (erro) {
        console.error("Erro ao atualizar contador do carrinho:", erro);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    carregarProdutos();
    atualizarContadorCarrinho();
});

