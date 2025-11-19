# Desafio CaixaVerso Lucas Resende de Sousa Amaral

Olá, obrigado por está analisando a minha implementação do desafio do CaixaVerso. As tecnologias usadas para implementar foram:
* Quarkus
* Java 21
* Maven 3.9
* Qute
* Mssql
* H2
* Smallrye-jwt
* Mockito
* JUnit
* Docker
* Docker-compose
* AWS

# 📝 Get Start

Você pode seguir este readme para analisar o que foi entregue nesse projeto, porém recomendo que fique até final, onde temos um bônus, além de ter feito a API do desáfio, também de forma a inovar decidi criar um front end onde chama a API desenvolvida para melhor exemplificar as suas funcionalidades.

O deploy dessa aplicação foi feito na AWS, pode ser localizado nesse link: (http://ec2-98-84-174-176.compute-1.amazonaws.com/documentacao)[http://ec2-98-84-174-176.compute-1.amazonaws.com/documentacao]

# 🐋 Configurando o ambiente

Para configurar o ambiente, siga os passos abaixo:

1. Certifique-se de ter o Docker e Docker Compose instalados em sua máquina.
2. Clone o repositorio localmente: 
```bash
git clone git@github.com:lucasres/caixa-verso-lucasresende.git
```
3. Entre na pasta do projeto e configure suas variaveis de ambiente:
| Variável         | Descrição                          | Exemplo               |
|------------------|------------------------------------|-----------------------|
| `DB_URL`         | Endereço do banco de dados         | `localhost`           |
| `DB_KIND`        | Especifica o tipo do banco         | `mssql ou h2`         |
| `DB_USER`        | Usuário do banco de dados          | `sa`                  |
| `DB_PASSWORD`    | Senha do banco de dados            | `sua_senha`           |



4. Execute o comando abaixo para iniciar os serviços definidos no arquivo `docker-compose.yml`:
```bash
docker-compose up
```

3. Após a execução, você verá os serviços sendo inicializados.

### Exemplo de execução:

![Exemplo de execução do Docker Compose](docker-compose.gif)