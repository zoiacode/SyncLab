package util;

public class JwtEncap {
    public String access_token;
    public String refresh_token;

    public JwtEncap(String accessToken, String refreshToken) {
        this.access_token = accessToken;
        this.refresh_token = refreshToken;
    }
}
