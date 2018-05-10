package nl.codebase.entities.iam.logout;

import lombok.extern.slf4j.Slf4j;
import nl.codebase.entities.common.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static nl.codebase.entities.common.FaceterConstants.AUTHENTICATED_COOKIE_NAME;
import static nl.codebase.entities.common.FaceterConstants.PARAM_ACCESS_TOKEN;
import static nl.codebase.entities.common.FaceterConstants.PARAM_REFRESH_TOKEN;

/**
 * Adds access and refresh tokens to logged_out_tokens table and removes cookies
 */
@Component
@Slf4j
public class IAMLogoutHandler implements LogoutHandler {

    private TokenService tokenService;

    @Autowired
    public IAMLogoutHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.addCookie(createImmediatelyExpiringCookie(PARAM_REFRESH_TOKEN));
        response.addCookie(createImmediatelyExpiringCookie(PARAM_ACCESS_TOKEN));
        response.addCookie(createImmediatelyExpiringCookie(AUTHENTICATED_COOKIE_NAME));

        Cookie[] cookies = request.getCookies();
        Arrays.stream(cookies)
                .filter(c -> PARAM_ACCESS_TOKEN.equals(c.getName()) || PARAM_REFRESH_TOKEN.equals(c.getName()))
                .forEach(c -> tokenService.logout(c.getName(), c.getValue()));
    }

    private Cookie createImmediatelyExpiringCookie(String name) {
        log.info("Removing cookie {}", name);
        Cookie refreshTokenCookie = new Cookie(name, null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        return refreshTokenCookie;
    }
}
