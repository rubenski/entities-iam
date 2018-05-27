package nl.codebase.entities.iam.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * Custom translator for OAuth exceptions. This translator sends more info to the frontend, so that the frontend
 * can distinguish between disabled accounts and bad credentials.
 */
@Slf4j
public class IAMResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.info("IAMResponseExceptionTranslator handler called");
        if (e instanceof InvalidGrantException) {
            InvalidGrantException invalidGrantException = (InvalidGrantException) e;
            return ResponseEntity.status(400)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new IAMOAuthException(invalidGrantException.getMessage()));
        } else {
            throw e;
        }
    }
}
