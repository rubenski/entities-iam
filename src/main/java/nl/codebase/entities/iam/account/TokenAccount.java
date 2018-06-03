package nl.codebase.entities.iam.account;

import lombok.Getter;

/**
 * TokenAccount provides a limited view on IAMAccount. TokenAccount will be encoded in the JWT token generated
 * by Spring. That is why it needs to be as small as possible.
 */
@Getter
public class TokenAccount {

    private long id;
    private String phone;
    private String firstName;
    private String lastName;

    public TokenAccount(IAMAccount iamAccount) {
        this.phone = iamAccount.getPhone();
        this.id = iamAccount.getId();
        this.firstName = iamAccount.getFirstName();
        this.lastName = iamAccount.getLastName();
    }
}
