## Documentação da API
A api responde no seguinte base-path: http://localhost:8080

A documentação via swagger está disponível em: http://localhost:8080/swagger-ui/index.html

## HubspotAuth
### Controller com endpoints para fluxo Oauth2 no Hubspot
#### **Retorna url de autorização do Hubspot**

```http
  GET /auth/hubspot/v1/authorization
```
#### Request

```
curl --location 'http://localhost:8080/auth/hubspot/v1/authorization'
```
#### Response
````
status: 200 OK
body: https://app.hubspot.com/oauth/authorize?client_id=client_id&scope=scopes&redirect_uri=redirectUri
````
````
status: 500 INTERNAL_SERVER_ERROR
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
![endpoint_get_authorization_uri](https://github.com/user-attachments/assets/05546fff-3ae0-461a-8cde-b33b5886baf0)

#### **Realiza o token-exchange**

```http
  GET /auth/hubspot/v1/token-exchange?code={code}
```
#### Request
```
curl --location 'http://localhost:8080/auth/hubspot/v1/token-exchange?code=code'
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `code`      | `string` | **Obrigatório**. Code informado pelo Hubspot no momento da autorização |

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
````
status: 400 BAD_REQUEST
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 401 UNAUTHORIZED
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 500 INTERNAL_SERVER_ERROR
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````

![endpoint_get_token_exchange](https://github.com/user-attachments/assets/6798af96-f546-4e4a-bfa4-267617aebf98)
## Contacts
### Controller com endpoints para ações do contato no Hubspot
#### **Cria um novo contato no Hubspot**
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
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `Authorization`      | `string` | **Obrigatório**. Header de autorização. Deve fornecer o access_token retornado pelo Hubspot no token_exchange |
| `body`      | `JSON` | **Obrigatório**. Corpo da requisição no formato JSON contendo dados do contato que será criado |

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
````
status: 400 BAD_REQUEST
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 401 UNAUTHORIZED
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 409 CONFLICT
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 429 TOO_MANY_REDIRECTS
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 500 INTERNAL_SERVER_ERROR
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````

![endpoint_create_contact](https://github.com/user-attachments/assets/b7c31143-de45-452e-a2ad-11e4a2050132)
## Webhook
### Controller com endpoints para receber webhooks do Hubspot
#### **Recebe uma chamada pelo webhook do Hubspot originada do evento Contact.creation**
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
````
status: 400 BAD_REQUEST
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 401 UNAUTHORIZED
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
````
status: 500 INTERNAL_SERVER_ERROR
body: 
{
    "title": "ERROR_TITLE",
    "message": "ERROR_MESSAGE",
    "requestDateTime": "2025-03-17T15:11:44.179758065"
}
````
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `body`      | `JSON` | **Obrigatório**. Corpo da requisição no formato JSON contendo dados do contato que será criado |
| `X-Hubspot-Signature`| `String` | **Obrigatório**. Header contendo a assinatura do webhook. Valor utilizado para validação de segurança do evento na api.|
| `X-Hubspot-Signature-Version`| `String` | **Obrigatório**. Header contendo a versão assinatura do webhook. Valor utilizado para validação de segurança do evento na api.|
| `X-Hubspot-Request-Timestamp`| `String` | Header contendo a data e hora do evneto do webhook. Utilizado na validação da v3 da assinatura.|

![endpoint_webhook_contacts_creation](https://github.com/user-attachments/assets/15bcb956-485b-42a1-a7be-9dacaf4db3cc)
