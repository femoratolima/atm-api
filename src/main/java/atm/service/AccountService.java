package atm.service;

import atm.repository.AccountRepository;
import atm.repository.OperationRepository;
import atm.repository.UserRepository;
import atm.domain.Account;
import atm.domain.Operation;
import atm.domain.User;
import atm.dto.BalanceDTO;
import atm.dto.ValueDTO;
import atm.enums.OperationType;
import atm.enums.WithdrawalFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    public Account create(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestClientException(String.format("User with email %s not found!", email)));

        accountRepository.findByOwner(user).ifPresent(userFound -> {
            throw new RestClientException(String.format("There's already a user registered with this email %s!",
                    user.getEmail()));
        });

        Account account = new Account();
        account.setOwner(user);

        return accountRepository.save(account);
    }

    public BalanceDTO withdrawal(String email, ValueDTO valueDTO) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestClientException(String.format("User with email %s not found!", email)));
        Account account = accountRepository.findByOwner(user)
                .orElseThrow(() -> new RestClientException(String.format("Account with owner %s not found!", email)));

        LocalDateTime time = LocalDateTime.now();

        validateWithdrawal(user, account, valueDTO.getValue(), time);

        Operation operation = new Operation();
        operation.setValue(valueDTO.getValue());
        operation.setOperationType(OperationType.WITHDRAWAL);
        operation.setOwner(user);
        operation.setFee(calculeFee(valueDTO.getValue()));
        operation.setDate(time);

        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = account.getBalance().subtract(valueDTO.getValue()).subtract(operation.getFee());
        account.setBalance(newBalance);
        operation.setAccountBalance(newBalance);

        operationRepository.save(operation);
        accountRepository.save(account);

        return new BalanceDTO(valueDTO.getValue(), operation.getFee(), oldBalance, account.getBalance());
    }

    private BigDecimal calculeFee(BigDecimal value) {
        Optional<BigDecimal> valueWithFee;

        valueWithFee = WithdrawalFee.stream()
                .filter(fee -> (value.compareTo(fee.getMinValue()) > 0 || value.compareTo(fee.getMinValue()) == 0)
                        && (value.compareTo(fee.getMaxValue()) < 0 || value.compareTo(fee.getMaxValue()) == 0))
                .map(fee -> value.multiply(fee.getFee()).divide(new BigDecimal(100), new MathContext(5)))
                .collect(Collectors.toList()).stream().findFirst();

        return valueWithFee.orElse(value);
    }

    private void validateWithdrawal(User user, Account account, BigDecimal value, LocalDateTime time) {
        if (isFirstMoneyWithdrawal(user.getEmail()) && value.compareTo(BigDecimal.valueOf(50L)) > 0) {
            throw new RestClientException("The first money withdrawal can be a maximum of $50,00!");
        }

        if (!canWithdrawal(user.getEmail(), time)) {
            throw new RestClientException("You have reached the maximum amount of 5 withdrawal a day!");
        }

        if (value.compareTo(BigDecimal.valueOf(300L)) > 0) {
            throw new RestClientException("The maximum amount for the money withdrawals must be $300,00!");
        }

        if (value.compareTo(BigDecimal.valueOf(1L)) < 0) {
            throw new RestClientException("The minimum value for any money withdrawal is $1,00!");
        }

        if (account.getBalance().compareTo(value) < 0) {
            throw new RestClientException(
                    String.format("Cannot withdrawal $%s. You only have $%s", value, account.getBalance()));
        }
    }

    private boolean canWithdrawal(String email, LocalDateTime time) {
        List<Operation> operations =
                operationRepository.findAllWithdrawalByDateAndOwnerEmail(
                        OperationType.WITHDRAWAL, email, time, time.minusDays(1));

        return operations.size() >= 5 ? Boolean.FALSE : Boolean.TRUE;
    }

    private boolean isFirstMoneyWithdrawal(String email) {
        List<Operation> operation = operationRepository.findAllByOwnerEmail(email);
        return !operation.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }
}
