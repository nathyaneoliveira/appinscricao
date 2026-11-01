# Sistema de Compras Internacionais

API REST de compras internacionais desenvolvida com Java 21, Spring Boot,
Spring Data JPA, MySQL e Spring Cloud OpenFeign. A cotação de venda USD/BRL é
obtida na AwesomeAPI; os valores são calculados com `BigDecimal` e registrados
com a cotação usada.

## Requisitos

- Java 21 ou superior
- Docker Desktop com Docker Compose

## Executar localmente

1. Copie `.env.example` para `.env` e substitua os valores das senhas e do
   segredo do token por valores fortes. Não compartilhe nem versiona o `.env`.
2. Na pasta do projeto, inicialize o MySQL:

   ```powershell
   docker compose up -d
   ```

3. Inicie a API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   O Flyway cria as tabelas e inclui um cliente para demonstração.

4. Para encerrar o banco sem remover os dados:

   ```powershell
   docker compose down
   ```

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/compras/simular` | Calcula uma compra sem persistir |
| `POST` | `/compras` | Valida limite e registra a compra |
| `GET` | `/clientes/{id}/compras` | Lista o histórico de compras |
| `GET` | `/clientes/{id}/limite` | Consulta utilização e saldo mensal |

O cliente de demonstração criado pela migration tem o UUID
`11111111-1111-4111-8111-111111111111`.

### Simular compra

```powershell
curl.exe -X POST http://localhost:8080/compras/simular `
  -H "Content-Type: application/json" `
  -d '{"valorUSD":200.00}'
```

### Registrar compra

```powershell
curl.exe -X POST http://localhost:8080/compras `
  -H "Content-Type: application/json" `
  -d '{"clienteId":"11111111-1111-4111-8111-111111111111","valorUSD":200.00}'
```

A API consulta a cotação `ask` atual; não aceite cotação enviada no corpo. A
taxa de serviço é 5% para compras abaixo de US$ 100 e 2% para compras a partir
de US$ 100. O tributo didático é 10% do valor convertido. O limite é R$ 10.000
por cliente a cada mês-calendário.

## Testes

```powershell
.\mvnw.cmd test
```

Os testes usam H2 em memória. A configuração principal usa MySQL local
configurado por `.env`.
