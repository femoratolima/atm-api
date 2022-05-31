package atm.dto;

import atm.domain.Account;
import atm.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UserAccountDTO {

    private String email;
    private String name;
    private UUID accountId;
    private BigDecimal accountBalance;

    public static UserAccountDTO from(User user, Account account) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setEmail(user.getEmail());
        userAccountDTO.setName(user.getName());
        userAccountDTO.setAccountId(account.getId());
        userAccountDTO.setAccountBalance(account.getBalance());

        return userAccountDTO;
    }
}
