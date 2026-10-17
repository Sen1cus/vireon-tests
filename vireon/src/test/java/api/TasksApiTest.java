
package api;

import config.TestConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TasksApiTest {


    private static AuthSession session;
    private static String accessToken;
    private static String userId;
    private static final TasksClient tasksClient = new TasksClient();

    @BeforeAll
    static void authenticate() {
        AuthClient authClient = new AuthClient();

        session = authClient.login(
                TestConfig.TEST_USER_EMAIL,
                TestConfig.TEST_USER_PASSWORD
        );

        accessToken = session.getAccessToken();
        userId = session.getUserId();
    }

    @Test
    @DisplayName("Authenticated user can retrieve permitted tasks")
    void getTasks_shouldReturnValidJson() {
        Response response = tasksClient
                .getTasks(accessToken)
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertTrue(
                response.contentType().contains("application/json"),
                "Response should have JSON content type"
        );

        List<Map<String, Object>> tasks =
                response.jsonPath().getList("$");

        assertNotNull(tasks, "Response should contain a JSON array");

        for (Map<String, Object> task : tasks) {
            assertInstanceOf(
                    String.class,
                    task.get("id"),
                    "Task ID should be a string"
            );
            assertInstanceOf(
                    String.class,
                    task.get("title"),
                    "Task title should be a string"
            );
            assertInstanceOf(
                    String.class,
                    task.get("user_id"),
                    "Task owner ID should be a string"
            );

            Object status = task.get("status");
            if (status != null) {
                assertInstanceOf(
                        String.class,
                        status,
                        "Task status should be a string or null"
                );
            }
        }
    }

    @Test
    @DisplayName("Unauthenticated user cannot retrieve tasks")
    void getTasks_withoutAuthenticationShouldReturnNoTasks() {
        Response response = tasksClient
                .getTasksWithoutAuthentication()
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Map<String, Object>> tasks =
                response.jsonPath().getList("$");

        assertNotNull(tasks, "Response should contain a JSON array");
        assertTrue(
                tasks.isEmpty(),
                "Unauthenticated request should not return tasks"
        );
    }

    @Test
    @DisplayName("Login with an incorrect password should fail")
    void loginWithIncorrectPassword_shouldReturn401() {
        AuthClient authClient = new AuthClient();

        Response response = authClient.loginExpectingFailure(
                TestConfig.TEST_USER_EMAIL,
                "DefinitelyWrongPassword123!"
        );

        assertEquals(400, response.getStatusCode());
        assertNull(
                response.jsonPath().getString("access_token"),
                "Failed login must not return an access token"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCredentials")
    void loginWithInvalidCredentialsShouldFail(
            String email,
            String password
    ) {
        Response response = new AuthClient()
                .loginExpectingFailure(email, password);

        assertEquals(400, response.statusCode());
        assertNull(response.jsonPath().getString("access_token"));
    }

    static Stream<Arguments> invalidCredentials() {
        return Stream.of(
                Arguments.of(
                        TestConfig.TEST_USER_EMAIL,
                        "DefinitelyWrongPassword123!"
                ),
                Arguments.of(
                        "missing-user@example.invalid",
                        "SomePassword123!"
                ),
                Arguments.of(
                        TestConfig.TEST_USER_EMAIL,
                        ""
                ),
                Arguments.of(
                        "",
                        "SomePassword123!"
                )
        );
    }
}
