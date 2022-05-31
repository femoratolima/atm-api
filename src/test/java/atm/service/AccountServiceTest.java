package atm.service;

import atm.domain.Account;
import atm.domain.Operation;
import atm.domain.User;
import atm.dto.BalanceDTO;
import atm.dto.ValueDTO;
import atm.enums.OperationType;
import atm.repository.AccountRepository;
import atm.repository.OperationRepository;
import atm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Autowired
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void create() {
        Account account = createAccount();

        when(userRepository.findById(any())).thenReturn(Optional.of(createUser()));
        when(accountRepository.findByOwner(any())).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(account);

        assertEquals(account, accountService.create("test@test.com"));

        verify(userRepository, times(1)).findById(any());
        verify(accountRepository, times(1)).findByOwner(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void withdrawal() {
        Account account = createAccount();
        Operation operation = createOperation();
        BalanceDTO balanceDTO = createBalanceDTO();

        when(userRepository.findById(any())).thenReturn(Optional.of(createUser()));
        when(accountRepository.findByOwner(any())).thenReturn(Optional.of(account));
        when(operationRepository.findAllByOwnerEmail(any())).thenReturn(List.of());
        when(operationRepository.findAllWithdrawalByDateAndOwnerEmail(any(), any(), any(), any())).thenReturn(List.of());
        when(accountRepository.save(any())).thenReturn(account);
        when(operationRepository.save(any())).thenReturn(operation);

        BalanceDTO result = accountService.withdrawal("test@test.com", createValueDTO());

        assertEquals(balanceDTO.getOperationValue(), result.getOperationValue());
        assertEquals(balanceDTO.getFee(), result.getFee());
        assertEquals(balanceDTO.getOldBalance(), result.getOldBalance());
        assertEquals(balanceDTO.getNewBalance(), result.getNewBalance());

        verify(userRepository, times(1)).findById(any());
        verify(accountRepository, times(1)).findByOwner(any());
        verify(operationRepository, times(1)).findAllByOwnerEmail(any());
        verify(operationRepository, times(1)).findAllWithdrawalByDateAndOwnerEmail(any(), any(), any(), any());
        verify(accountRepository, times(1)).save(any());
        verify(operationRepository, times(1)).save(any());
    }

    private Operation createOperation() {
        Operation operation = new Operation();
        operation.setOperationType(OperationType.WITHDRAWAL);
        operation.setOwner(createUser());
        operation.setValue(BigDecimal.TEN);
        operation.setDate(LocalDateTime.now());

        return operation;
    }


    private Account createAccount() {
        Account account = new Account();
        account.setOwner(createUser());
        account.setId(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"));

        return account;
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test");

        return user;
    }

    private ValueDTO createValueDTO() {
        ValueDTO valueDTO = new ValueDTO();
        valueDTO.setValue(BigDecimal.TEN);

        return valueDTO;
    }

    private BalanceDTO createBalanceDTO() {
        return new BalanceDTO(BigDecimal.TEN, BigDecimal.valueOf(0.3),
                BigDecimal.valueOf(1000), BigDecimal.valueOf(989.7));
    }
}
