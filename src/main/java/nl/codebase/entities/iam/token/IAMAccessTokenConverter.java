package nl.codebase.entities.iam.token;

import nl.codebase.entities.iam.account.IAMAccount;
import nl.codebase.entities.iam.account.TokenAccount;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;


public class IAMAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        IAMAccount iamAccount = (IAMAccount) authentication.getPrincipal();
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("account", new TokenAccount(iamAccount));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extraInfo);

        return super.enhance(accessToken, authentication);
    }
}
