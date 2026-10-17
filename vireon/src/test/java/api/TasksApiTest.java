
package api;

import config.TestConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class TasksApiTest {


    private static AuthSession session;
    private static String accessToken;
    private static String userId;

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
        Response response = given()
                .baseUri(TestConfig.BACKEND_URL)
                .basePath("/rest/v1")
                .header("apikey", TestConfig.PUBLISHABLE_KEY)
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json")
                .queryParam("select", "id,title,status,user_id")
                .queryParam("limit", 10)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Map<String, Object>> tasks =
                response.jsonPath().getList("$");

        assertNotNull(tasks, "Response should contain a JSON array");

        for (Map<String, Object> task : tasks) {
            assertEquals(
                    userId,
                    task.get("user_id"),
                    "User should only see their own tasks"
            );
            assertNotNull(task.get("id"), "Task ID must exist");
            assertNotNull(task.get("title"), "Task title must exist");
            assertNotNull(task.get("user_id"), "Task owner must exist");
        }
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

}
