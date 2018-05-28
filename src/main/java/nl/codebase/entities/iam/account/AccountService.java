package nl.codebase.entities.iam.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import nl.codebase.entities.iam.IAMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class AccountService {

    private OkHttpClient httpClient;
    private static final String ACCOUNT_API_BASE_URL = "http://localhost:8083/api/account/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int STATUS_OK = 200;

    @Autowired
    public AccountService(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private String base64(String encodePlz) {
        return Base64.getEncoder().encodeToString(encodePlz.getBytes());
    }

    public UserDetails authenticate(String email, String password) throws UsernameNotFoundException {

        String url = ACCOUNT_API_BASE_URL + base64(email) + "/" + base64(password);
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = httpClient.newCall(request).execute();
            ResponseBody body = response.body();
            int code = response.code();
            if (code != STATUS_OK) {
                throw new UsernameNotFoundException("Account for email " + email + " not found.");
            }
            return OBJECT_MAPPER.readValue(body.string(), IAMAccount.class);
        } catch (IOException e) {
            throw new IAMException(e);
        }
    }
}
