package nl.codebase.entities.iam.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM f_user WHERE email = ?";

    private final DataSource dataSource;

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public User findUserByEmail(String email) {
        return getJdbcTemplate().query(SQL_SELECT_BY_ID, preparedStatement -> {
            preparedStatement.setString(1, email);
        }, rs -> {

            if(!rs.next()) {
                // UsernameNotFoundException will trigger Spring to return a 400 to the client, which
                // is the same response as for filling out bad credentials, so this is what we need.
                throw new UsernameNotFoundException("User not found");
            }

            User user = new User();
            user.setUuid(rs.getString("uuid"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setRoles(rs.getString("roles"));
            user.setLocked(rs.getBoolean("locked"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setAccountExpired(rs.getBoolean("account_expired"));
            user.setPassword(rs.getString("pw"));
            return user;
        });
    }
}
