# case-tecnico-back-end-meetime

API de Integração com HubSpot
Esta API foi desenvolvida em Java (versão 21) utilizando Spring Boot, H2, JPA, Lombok e Validation. Ela permite a integração com o HubSpot para criar e gerenciar contatos e validar webhooks enviados pela plataforma.

## Instruções para Execução do Projeto

Pré-requisitos

Java 21: Certifique-se de que você possui o Java 21 instalado em sua máquina.

Maven: Certifique-se de que você possui o Maven instalado em sua máquina.

### Instalação e execução

#### Execução com Maven
````
git clone https://github.com/Rafa-Moura/case-tecnico-back-end-meetime.git
cd case-tecnico-back-end-meetime
mvn springboot:run
````
#### Execução com docker

Necessário possuir o Docker instalado
````
git clone https://github.com/Rafa-Moura/case-tecnico-back-end-meetime.git
cd case-tecnico-back-end-meetime
docker compose up
````

## Stack utilizada

**Back-end:** Java, Springboot, JPA, H2, Lombok e Validation

Para esse projeto, optei pela utilização do Java em sua versão 21, Springboot em sua versão 3.4.3, JPA (gerenciado pelo spring-data-jpa), banco de dados em memória H2, Lombok para reduzir um pouco do boilerplate e Validation para realizar validação do endpoint de criação do Contato.

## Decisões técnicas

**O RedirectUri cadastrado dentro do App público do Hubspot para realizar o fluxo Oauth2 foi o mesmo utilizado na API (http://localhost:8080/auth/hubspot/v1/token-exchange?code=code) apenas para fins de facilitar o uso. Em um cenário real, visando a estratégia de apis, deveria ser utilizado o callbackUri de um front-end para que através do front-end o code fosse repassado para o back-end em um endpoint protegido.**

Para solução do Oauth2 com a Hubspot, optei por não utilizar as dependências do Spring Security Oauth2 Client e Spring Security Oauth2 Resource server. Uma vez que o Hubspot não segue 100% dos padrões Oauth2 e OIDC, seriam necessárias algumas customizações nas abstrações que as dependências do Spring Security fornecem. Desse modo, optei por fazer o controle do fluxo manualmente utilizando o HttpClient do java.net.http

## Melhorias e sugestões

Para melhorar a utilização e o ciclo de vida de segurança da aplicação, poderiam ser realizadas algumas alteraçãoes:

### Segurança:

- Ao gerar o access_token utilizando o code, deveria fazer o introspect do access_token e armazenar na base: User e Refresh_token. Devolvendo ao client (access_token, token_type, expires_in e user)
  ![sugestao_endpoint_melhoria_token_exchange](https://github.com/user-attachments/assets/15f413f6-0458-4c65-baa9-5c2d1129fc10)
- Quando a api retornar o client que o token está expirado, o client chama o endpoint de refresh_token passando o user. A aplicação gera novo access_token pelo refresh_token recuperado da base.
  ![endpoint_refresh_token](https://github.com/user-attachments/assets/0b057f9f-90d7-47cb-8e5e-d83d955c2848)
