
package api;

import config.TestConfig;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthClient {

    public AuthSession login(String email, String password) {

        Response response = given()
                .baseUri(TestConfig.BACKEND_URL)
                .header("apikey", TestConfig.PUBLISHABLE_KEY)
                .contentType("application/json")
                .body(Map.of(
                        "email", email,
                        "password", password
                ))
                .when()
                .post("/auth/v1/token?grant_type=password");

        response.then().statusCode(200);

        String accessToken =
                response.jsonPath().getString("access_token");

        String userId =
                response.jsonPath().getString("user.id");

        return new AuthSession(accessToken, userId);
    }

    public Response loginExpectingFailure(String email, String password) {
        return given()
                .baseUri(TestConfig.BACKEND_URL)
                .header("apikey", TestConfig.PUBLISHABLE_KEY)
                .contentType("application/json")
                .body(Map.of(
                        "email", email,
                        "password", password
                ))
                .when()
                .post("/auth/v1/token?grant_type=password");
    }

}