package nl.codebase.entities.iam.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class User implements UserDetails {

    private final ObjectMapper mapper = new ObjectMapper();

    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String roles;
    private String password;
    private boolean accountExpired;
    private boolean locked;
    private boolean enabled;

    private Collection<SimpleGrantedAuthority> grantedAuthorities;

    public User(String email) {
        this.email = email;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            if(grantedAuthorities == null) {
                GrantsWrapper grantsWrapper = mapper.readValue(roles, GrantsWrapper.class);
                grantedAuthorities = grantsWrapper.getGrants().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
            return grantedAuthorities;
        } catch (IOException e) {
            log.error("Could not deserialize grants from database", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Getter
    @Setter
    private static class GrantsWrapper {
        private List<String> grants;
    }
}
