async function carregarCarrinho() {
    const token = localStorage.getItem("token");

    try {
        const resposta = await fetch("http://localhost:3000/carrinho", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!resposta.ok) {
            throw new Error("Erro ao buscar carrinho");
        }

        const carrinho = await resposta.json();

        const cartArea = document.getElementById("cartArea");
        cartArea.innerHTML = "";

        if (!carrinho.items || carrinho.items.length === 0) {
            cartArea.innerHTML = "<p>Seu carrinho está vazio.</p>";
            return;
        }

        carrinho.items.forEach(item => {
            const produto = item.product // depende do seu DTO de resposta
            const card = document.createElement("div");
            card.className = "cart-item";
            card.innerHTML = `
                <img src="${produto.imgURL}" alt="${produto.name}" style="width:120px;height:auto;" />
                <h3>${produto.name}</h3>
                <p>${produto.description}</p>
                <p><strong>Preço:</strong> R$ ${item.price}</p>
                <p><strong>Quantidade:</strong> ${item.quantity}</p>
                
            `;
            cartArea.appendChild(card);
        });

        // Mostrar ações do carrinho (checkout, etc.)
        const cartActions = document.getElementById("cartActions");
        cartActions.innerHTML = `
            <button onclick="finalizarCarrinho()">Finalizar Compra</button>
        `;
    } catch (erro) {
        console.error("Erro ao carregar carrinho:", erro);
        alert("Não foi possível carregar o carrinho.");
    }
}

async function finalizarCarrinho() {
    const token = localStorage.getItem("token");

    try {
        const resposta = await fetch("http://localhost:3000/carrinho/checkout", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!resposta.ok) {
            throw new Error("Erro ao finalizar compra");
        }

        alert("Compra finalizada com sucesso!");
        carregarCarrinho(); // recarrega para mostrar carrinho vazio
    } catch (erro) {
        console.error("Erro ao finalizar compra:", erro);
        alert("Não foi possível finalizar a compra.");
    }
}

// Carregar carrinho quando a página abrir
window.addEventListener("DOMContentLoaded", carregarCarrinho);