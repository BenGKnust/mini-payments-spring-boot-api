# mini-payments-api

A small REST API for accounts and money transfers, built with Java and Spring Boot.
Built as a practice project to learn REST design, relational databases, transactions and testing.

## Features
- Create accounts and look them up by ID
- Transfer money between accounts in a single database transaction
- Input validation and clear HTTP status codes (400, 404, 422)
- Unit tests for the transfer logic (JUnit 5, Mockito)

## Tech stack
Java [25/21], Spring Boot, Spring Data JPA, H2 (in-memory database), Maven, JUnit 5, Mockito

## Run it
```
.\mvnw.cmd spring-boot:run      (Windows)
./mvnw spring-boot:run          (macOS / Linux)
```
The API runs on http://localhost:8080.

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | /accounts | Create an account `{"owner":"Anna","balance":100}` |
| GET | /accounts/{id} | Get an account and its balance |
| POST | /transfers | Transfer money `{"fromAccountId":1,"toAccountId":2,"amount":30}` |

## Error responses
- 400: invalid input (negative amount, same account for sender and receiver)
- 404: account not found
- 422: insufficient funds

## Run the tests
```
.\mvnw.cmd test
```

## Design notes
- Transfers run in one `@Transactional` method, so both balance updates succeed or neither does.
- Balances use `BigDecimal` instead of `double` to avoid rounding errors.
- Business logic is in `TransferService`, separate from the controller and database, so it can be unit tested with a mocked repository.
- Known limitation: concurrent transfers on the same account are not locked. A production system would add optimistic or pessimistic locking.