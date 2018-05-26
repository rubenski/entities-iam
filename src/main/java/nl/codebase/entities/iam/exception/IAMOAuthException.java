package nl.codebase.entities.iam.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = IAMOAuthExceptionSerializer.class)
public class IAMOAuthException extends OAuth2Exception {

    public IAMOAuthException(String msg) {
        super(msg);
    }
}
