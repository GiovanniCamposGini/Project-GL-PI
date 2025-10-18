document.getElementById('cadastroForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const cpf = document.getElementById('cpf').value;
    const name = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('senha').value;
    const confirmPassword = document.getElementById('confirma').value;
    const groups = document.getElementById('cargo').value.toUpperCase() === 'ADM' ? 'ADMIN' : 'USER';

    if (password !== confirmPassword) {
        alert('As senhas não coincidem!');
        return;
    }

    const data = {
        cpf: cpf,
        name: name,
        email: email,
        password: password,
        groups: groups
    };

    fetch('http://localhost:3000/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                alert('Cadastro realizado com sucesso!');
                window.location.href = 'login.html';
            } else {
                response.text().then(text => {
                    alert('Erro ao cadastrar: ' + text);
                });
            }
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            alert('Erro de conexão com o servidor.');
        });     });