
package api;

import config.TestConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TasksClient {

    public Response getTasks(String accessToken) {
        return given()
                .baseUri(TestConfig.BACKEND_URL)
                .basePath("/rest/v1")
                .header("apikey", TestConfig.PUBLISHABLE_KEY)
                .header("Authorization", "Bearer " + accessToken)
                .accept("application/json")
                .queryParam("select", "id,title,status,user_id")
                .queryParam("limit", 10)
                .when()
                .get("/tasks");
    }

    public Response getTasksWithoutAuthentication() {
        return given()
                .baseUri(TestConfig.BACKEND_URL)
                .basePath("/rest/v1")
                .header("apikey", TestConfig.PUBLISHABLE_KEY)
                .accept("application/json")
                .queryParam("select", "id,title,status,user_id")
                .queryParam("limit", 10)
                .when()
                .get("/tasks");
    }
}
