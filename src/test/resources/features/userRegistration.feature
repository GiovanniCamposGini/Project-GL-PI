Feature: Registro de usuário

  Scenario: Registrar um novo usuário com dados válidos
    Given que o sistema não possui um usuário com o email "joao@example.com"
    When eu registro um novo usuário com os seguintes dados:
      | name  | email             | password | cpf           | groups |
      | João  | joao@example.com | 123456   | 485.009.328-05 | USER   |
    Then o sistema deve retornar status 200
