
package config;

public class TestConfig {

    // URL webové aplikace – používá ji AppHealthCheckApiTest
    public static final String BASE_URL =
            "https://vireonos.lovable.app";

    // URL backendu – používá ji TasksApiTest
    public static final String BACKEND_URL =
            System.getenv("APP_BACKEND_URL");

    public static final String PUBLISHABLE_KEY =
            System.getenv("APP_PUBLISHABLE_KEY");

    public static final String TEST_USER_EMAIL =
            System.getenv("TEST_USER_EMAIL");

    public static final String TEST_USER_PASSWORD =
            System.getenv("TEST_USER_PASSWORD");
}
