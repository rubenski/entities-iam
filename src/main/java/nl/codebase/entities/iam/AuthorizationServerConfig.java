package nl.codebase.entities.iam;

import lombok.extern.slf4j.Slf4j;
import nl.codebase.entities.iam.exception.IAMResponseExceptionTranslator;
import nl.codebase.entities.iam.token.IAMAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;

    @Value("${security.jwt.grant-type}")
    private String grantType;

    @Value("${security.jwt.scope-read}")
    private String scopeRead;

    @Value("${security.jwt.scope-write}")
    private String scopeWrite = "write";

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Value("${security.access-token-expiry-seconds}")
    private int accessTokenExpiryTimeSeconds;

    @Value("${security.refresh-token-expiry-seconds}")
    private int refreshTokenExpiryTimeSeconds;

    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter accessTokenConverter;
    private IAMAccessTokenConverter iamAccessTokenConverter;

    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager,
                                     JwtAccessTokenConverter accessTokenConverter,
                                     IAMAccessTokenConverter iamAccessTokenConverter) {
        this.authenticationManager = authenticationManager;
        this.accessTokenConverter = accessTokenConverter;
        this.iamAccessTokenConverter = iamAccessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer
                .inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(grantType.split(","))
                .accessTokenValiditySeconds(accessTokenExpiryTimeSeconds)
                .refreshTokenValiditySeconds(refreshTokenExpiryTimeSeconds)
                .scopes(scopeRead, scopeWrite)
                .resourceIds(resourceIds);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        super.configure(security);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(iamAccessTokenConverter));
        endpoints.tokenStore(tokenStore())
                //.accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(enhancerChain)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(new IAMResponseExceptionTranslator());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}