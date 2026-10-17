
package api;

public class AuthSession {

    private final String accessToken;
    private final String userId;

    public AuthSession(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserId() {
        return userId;
    }
}
