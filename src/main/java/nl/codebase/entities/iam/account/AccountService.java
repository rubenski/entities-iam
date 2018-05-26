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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class AccountService implements UserDetailsService {


    private OkHttpClient httpClient;
    // TODO: Configure URL to go via the proxy (8082) and then fix auth problem
    private static final String USER_API_BASE_URL = "http://localhost:8083/api/account/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // TODO: Needed later?
    private static final String TOKEN_AUTH_USERNAME = "testjwtclientid";
    private static final String TOKEN_AUTH_PASSWORD = "XY7kmzoNzl100";


    @Autowired
    public AccountService(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private String base64(String encodePlz) {
        return Base64.getEncoder().encodeToString(encodePlz.getBytes());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String url = USER_API_BASE_URL + base64(email);

        Request request = new Request.Builder()
                .url(url)
                //.addHeader("Authorization", "Basic " + base64(TOKEN_AUTH_USERNAME + ":" + TOKEN_AUTH_PASSWORD))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            ResponseBody body = response.body();
            IAMAccount iamAccount = OBJECT_MAPPER.readValue(body.bytes(), IAMAccount.class);
            return iamAccount;
        } catch (IOException e) {
            throw new IAMException(e);
        }
    }
}
