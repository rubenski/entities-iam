package nl.codebase.entities.iam.user;

public interface UserDao {

    User findUserByEmail(String uuid);

}
