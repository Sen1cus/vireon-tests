# Vireon API Test Automation

A Java-based API test automation project for testing the Vireon application. The project uses RestAssured and JUnit 5 to verify API behavior, authentication, and response data.

## Tech Stack

- Java 17
- Maven
- JUnit 5
- RestAssured

## Project Structure

```text
vireon/src/test/java/
├── api/
│   ├── AuthClient.java
│   ├── AuthSession.java
│   ├── TasksClient.java
│   └── TasksApiTest.java
├── config/
│   └── TestConfig.java
└── AppHealthCheckApiTest.java
```

## Test Coverage

### Application Health Check

- Verifies that the application is reachable.
- Checks the HTTP status code and HTML response.
- Verifies the page title and response time.

### Authentication

- Verifies successful login using valid test credentials.
- Verifies unsuccessful login attempts with invalid credentials.
- Uses parameterized tests to cover multiple invalid credential combinations.
- Checks that unsuccessful login attempts do not return an access token.

### Tasks API

- Retrieves tasks using an authenticated request.
- Checks the HTTP status code and JSON content type.
- Validates the structure and data types of returned task fields.
- Checks the response to an unauthenticated request. The current backend returns an empty JSON array for the tested request.

## Configuration

The tests use environment variables to keep configuration and credentials outside the source code.

Configure the following variables before running the tests:

| Variable | Description |
|---|---|
| `APP_BACKEND_URL` | Backend base URL |
| `APP_PUBLISHABLE_KEY` | Supabase publishable key |
| `TEST_USER_EMAIL` | Email address of the test account |
| `TEST_USER_PASSWORD` | Password of the test account |

The test account must be valid for authentication tests.

**Do not commit passwords, service-role keys, or other privileged credentials to the repository.**

## Running the Tests

Requirements:

- JDK 17
- Maven
- Network access to the application backend
- Valid environment variables for authentication tests

Clone the repository:

```bash
git clone https://github.com/Sen1cus/vireon-tests.git
cd vireon-tests/vireon
```

Configure the required environment variables, then run:

```bash
mvn test
```

Maven downloads the project dependencies and executes the test suite.

## Design

The project separates API communication from test assertions:

- `AuthClient` handles authentication requests.
- `AuthSession` stores the access token and authenticated user ID.
- `TasksClient` encapsulates HTTP requests to the tasks endpoint.
- `TasksApiTest` contains API test scenarios and assertions.
- `TestConfig` centralizes environment-based configuration.

This separation keeps HTTP request details reusable and allows tests to focus on expected API behavior.

## Scope and Limitations

This is a portfolio project demonstrating API test automation fundamentals. It currently focuses on application availability, authentication, and task retrieval.

The tests use a real backend and depend on its availability, configuration, permissions, and test data. The current unauthenticated tasks test verifies the observed empty response; it does not independently prove that all backend access-control rules are correctly configured.
