document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('loginSenha').value;

    const data = {
        email: email,
        password: password
    };

    fetch('http://localhost:3000/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Login inválido');
            }
        })
        .then(result => {
            localStorage.setItem('token', result.token);
            alert('Login realizado com sucesso!');
            window.location.href = 'index.html';
        })
        .catch(error => {
            console.error('Erro no login:', error);
            alert('E-mail ou senha incorretos.');
        });
});