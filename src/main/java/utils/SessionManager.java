package utils;

import java.util.prefs.Preferences;

public class SessionManager {
    private static final String ACCESS_TOKEN_KEY = "jwtAccessToken";
    private static final String REFRESH_TOKEN_KEY = "jwtRefreshToken";
    private static final Preferences prefs = Preferences.userNodeForPackage(SessionManager.class);

    public static void setTokens(String accessToken, String refreshToken) {
        prefs.put(ACCESS_TOKEN_KEY, accessToken);
        prefs.put(REFRESH_TOKEN_KEY, refreshToken);
    }

    public static String getAccessToken() {
        return prefs.get(ACCESS_TOKEN_KEY, null);
    }

    public static String getRefreshToken() {
        return prefs.get(REFRESH_TOKEN_KEY, null);
    }

    public static void loadTokens() {
        String accessToken = prefs.get(ACCESS_TOKEN_KEY, null);
        String refreshToken = prefs.get(REFRESH_TOKEN_KEY, null);
    }

    public static boolean isLoggedIn() { // tchouf token mawjoud walle le
        String accessToken = getAccessToken();
        return accessToken != null && JWTUtils.isTokenValid(accessToken);
    }

    public static void logout() {
        prefs.remove(ACCESS_TOKEN_KEY);
        prefs.remove(REFRESH_TOKEN_KEY);
    }

    public static void refreshAccessToken(String newAccessToken) {
        prefs.put(ACCESS_TOKEN_KEY, newAccessToken);
    }
}
