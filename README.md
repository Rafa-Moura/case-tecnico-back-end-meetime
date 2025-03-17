# case-tecnico-back-end-meetime

API de Integração com HubSpot
Esta API foi desenvolvida em Java (versão 21) utilizando Spring Boot, H2, JPA, Lombok e Validation. Ela permite a integração com o HubSpot para criar e gerenciar contatos e validar webhooks enviados pela plataforma.

Instruções para Execução do Projeto

Pré-requisitos

Java 21: Certifique-se de que você possui o Java 21 instalado em sua máquina.

Maven: Certifique-se de que você possui o Maven instalado em sua máquina.# case-tecnico-back-end-meetime

API de Integração com HubSpot
Esta API foi desenvolvida em Java (versão 21) utilizando Spring Boot, H2, JPA, Lombok e Validation. Ela permite a integração com o HubSpot para criar e gerenciar contatos e validar webhooks enviados pela plataforma.

Instruções para Execução do Projeto

Pré-requisitos

Java 21: Certifique-se de que você possui o Java 21 instalado em sua máquina.

Maven: Certifique-se de que você possui o Maven instalado em sua máquina.

## Instalação e execução

````
git clone https://github.com/Rafa-Moura/case-tecnico-back-end-meetime.git
cd case-tecnico-back-end-meetime
mvn springboot:run
````

## Documentação da API
A api responde no seguinte base-path: http://localhost:8080

## HubspotAuth
### Controller com endpoints para fluxo Oauth2 no Hubspot
#### Retorna url de autorização do Hubspot

```http
  GET /auth/hubspot/v1/authorization
```
#### Request

```
curl --location 'http://localhost:8080/auth/hubspot/v1/authorization'
```
````
status: 200 OK
body: https://app.hubspot.com/oauth/authorize?client_id=client_id&scope=scopes&redirect_uri=redirectUri
````
![endpoint_get_authorization_uri](https://github.com/user-attachments/assets/05546fff-3ae0-461a-8cde-b33b5886baf0)

#### Realiza o token-exchange

```http
  GET /auth/hubspot/v1/token-exchange?code={code}
```
#### Request
```
curl --location 'http://localhost:8080/auth/hubspot/v1/token-exchange?code=code'
```
#### Response
````
status: 200 OK
body:
{
    "access_token": "AAAeeerrr44488877",
    "expires_in": 1800,
    "token_type": "Bearer",
    "refresh_token": "Aaeeerrrrrtttttt"
}
````

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `code`      | `string` | **Obrigatório**. Code informado pelo Hubspot no momento da autorização |

![endpoint_get_token_exchange](https://github.com/user-attachments/assets/6798af96-f546-4e4a-bfa4-267617aebf98)
## Contacts
### Controller com endpoints para ações do contato no Hubspot
#### Cria um novo contato no Hubspot
```http
  POST /api/contacts/v1
```
#### Request
```
curl --location 'http://localhost:8080/api/contacts/v1' \
--header 'Authorization: Bearer token' \
--header 'Content-Type: application/json' \
--data-raw '{
  "properties": {
    "email": "teste_email@teste.com",
    "lastname": "Teste",
    "firstname": "Teste"
  }
}'
```
#### Response
````
status: 201 CREATED
body:
{
  "id": 1,
  "contactId": "1234"
  "properties": {
    "email": "bh@hubspot.com",
    "lastname": "Brian",
    "firstname": "Halligan"
  }
}
````
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `Authorization`      | `string` | **Obrigatório**. Header de autorização. Deve fornecer o access_token retornado pelo Hubspot no token_exchange |
| `body`      | `JSON` | **Obrigatório**. Corpo da requisição no formato JSON contendo dados do contato que será criado |

![endpoint_create_contact](https://github.com/user-attachments/assets/b7c31143-de45-452e-a2ad-11e4a2050132)
## Webhook
### Controller com endpoints para receber webhooks do Hubspot
#### Recebe uma chamada pelo webhook do Hubspot originada do evento Contact.creation
```http
  POST /webhook/contacts/v1
```
#### Request
```
curl --location 'http://localhost:8080/webhook/contacts/v1' \
--header 'X-HubSpot-Signature: 82167b8cfdb4cdb1292b34a6c9653a0fc32d2958985ca048debedd8ac168a5c2' \
--header 'X-HubSpot-Signature-Version: v3' \
--header 'X-HubSpot-Request-Timestamp: 2025-03-16T12:12:23' \
--header 'Content-Type: application/json' \
--data '[
    {
        "objectId": 1246978,
        "changeSource": "IMPORT",
        "eventId": 3816279480,
        "subscriptionId": 22,
        "portalId": 33,
        "appId": 1160452,
        "occurredAt": 1462216307945,
        "eventType": "contact.creation",
        "attemptNumber": 0
    }
]'
```
#### Response
````
status: 201 CREATED
````
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `body`      | `JSON` | **Obrigatório**. Corpo da requisição no formato JSON contendo dados do contato que será criado |
| `X-Hubspot-Signature`| `String` | **Obrigatório**. Header contendo a assinatura do webhook. Valor utilizado para validação de segurança do evento na api.|
| `X-Hubspot-Signature-Version`| `String` | **Obrigatório**. Header contendo a versão assinatura do webhook. Valor utilizado para validação de segurança do evento na api.|
| `X-Hubspot-Request-Timestamp`| `String` | Header contendo a data e hora do evneto do webhook. Utilizado na validação da v3 da assinatura.|

![endpoint_webhook_contacts_creation](https://github.com/user-attachments/assets/15bcb956-485b-42a1-a7be-9dacaf4db3cc)
## Stack utilizada

**Back-end:** Java, Springboot, JPA, H2, Lombok e Validation

Para esse projeto, optei pela utilização do Java em sua versão 21, Springboot em sua versão 3.4.3, JPA (gerenciado pelo spring-data-jpa), banco de dados em memória H2, Lombok para reduzir um pouco do boilerplate e Validation para realizar validação do endpoint de criação do Contato.

## Decisões técnicas

Para solução do Oauth2 com a Hubspot, optei por não utilizar as dependências do Spring Security Oauth2 Client e Spring Security Oauth2 Resource server. Uma vez que o Hubspot não segue 100 dos padrões Oauth2 e OIDC, seriam necessárias algumas customizações nas abstrações que as dependências do Spring Security fornecem. Desse modo, optei por fazer o controle fluxo manualmente utilizando o HttpClient do java.net.http

## Melhorias e sugestões

Para melhorar a utilização e o ciclo de vida de segurança da aplicação, poderiam ser realizadas algumas alteraçãoes:

### Segurança:

- Ao gerar o access_token utilizando o code, deveria fazer o introspect do access_token e armazenar na base: User e Refresh_token. Devolvendo ao client (access_token, token_type, expires_in e user)
  ![sugestao_endpoint_melhoria_token_exchange](https://github.com/user-attachments/assets/15f413f6-0458-4c65-baa9-5c2d1129fc10)
- Quando a api retornar o client que o token está expirado, o client chama o endpoint de refresh_token passando o user. A aplicação gera novo access_token pelo refresh_token recuperado da base.
  ![endpoint_refresh_token](https://github.com/user-attachments/assets/0b057f9f-90d7-47cb-8e5e-d83d955c2848)

