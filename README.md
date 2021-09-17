# Financeiro
Sistema de contas a pagar para teste da empresa Deliver IT

# Instruções para executar localmente a Api
1- Subir container de banco de dados postgres 10.0
  - Navegar até a pasta onde contém o docker-compose.yaml "financeiro/bin/postgres/"
  - Executar o comando "docker-compose up -d"
2- Criar o banco de dados "financeiro"

# Segurança da Api
1- A Api utiliza o tipo de seguraça basic do Spring Security sendo necessário o envio do Header Authorization com as credenciais encoded username: "deliverit" password: "deliverit"
em todas as requisições.

# Acessar a documentação da Api (OpenAPI)
1- http://localhost:8080/financeiro-backend/swagger-ui.html
2- Acessar usandos as credenciais username: "deliverit" password: "deliverit"

# CI/CD
1- a aplicação contém um arquivo Jenkinsfile onde é feito o build e empacotamento dos arquivos e criação de imagem docker para o deploy nos ambientes
Obs.: Alterar o repositório para criação da imagem
2- contém um deployment do kubernates para deploy da aplicação em ambientes cloud.



