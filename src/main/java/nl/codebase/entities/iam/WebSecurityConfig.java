package nl.codebase.entities.iam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.encoding-strength}")
    private Integer encodingStrength;

    @Value("${security.security-realm}")
    private String securityRealm;

    @Value("${security.signing-key}")
    private String signingKey;

    @Value("${security.jwt.client-id")
    private String jwtClient;

    @Value("${security.jwt.client-secret}")
    private String secret;

    @Value("${logout.success.url}")
    private String logoutSuccessUrl;

    private final UserDetailsService userDetailsService;
    private LogoutHandler logoutHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, LogoutHandler logoutHandler) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new ShaPasswordEncoder(encodingStrength));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .logout()
                .addLogoutHandler(logoutHandler)
                .logoutUrl("/logout")
                .logoutSuccessUrl(logoutSuccessUrl);
    }
}
