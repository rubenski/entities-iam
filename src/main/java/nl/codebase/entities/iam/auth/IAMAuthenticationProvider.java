package nl.codebase.entities.iam.auth;

import nl.codebase.entities.iam.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Authentication provider that checks with the Account service whether the account exists and whether the login attempt
 * has valid credentials. In order to validate this we must retrieve both the hashed password and the salt from
 * the Account api.
 */
@Component
public class IAMAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private AccountService accountService;

    @Autowired
    public IAMAuthenticationProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return accountService.authenticate(username, authentication.getCredentials().toString());
    }
}
