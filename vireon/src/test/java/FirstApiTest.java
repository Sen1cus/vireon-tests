import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;

public class FirstApiTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://vireonos.lovable.app";
    }
    @Test
    @DisplayName("Aplikace vireonos.lovable.app by měla být dostupná a vracet 200 OK")
    void checkAppAvailability_shouldReturn200() {
        given()
                .header("User-Agent", "JUnit-RestAssured-Test")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .time(lessThan(3000L));
    }
    @Test
    @DisplayName("Odeslání GET requestu, přijetí response, kontrola status kódu a obsahu")
    void testGetVireonosApp() {
        Response response = given()
                .header("User-Agent", "Mozilla/5.0 (RestAssured-Test)")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(containsString("<title>VireonOS</title>"))
                .extract()
                .response();

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Content-Type: " + response.getContentType());
    }
}