package atm.service;

import atm.domain.Account;
import atm.domain.User;
import atm.dto.NameDTO;
import atm.dto.UserAccountDTO;
import atm.dto.UserDTO;
import atm.repository.AccountRepository;
import atm.repository.OperationRepository;
import atm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Autowired
    @InjectMocks
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Test
    void findById() {
        User user = createUser();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user), userService.findById(user.getEmail()));

        verify(userRepository, times(1)).findById(any());
    }

    @Test
    void create() {
        User user = createUser();
        Account account = createAccount();

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(accountService.create(any())).thenReturn(createAccount());

        UserAccountDTO userAccountCreated = userService.create(createUserDTO());

        assertEquals(user.getEmail(), userAccountCreated.getEmail());
        assertEquals(user.getName(), userAccountCreated.getName());
        assertEquals(account.getId(), userAccountCreated.getAccountId());
        assertEquals(account.getBalance(), userAccountCreated.getAccountBalance());

        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
        verify(accountService, times(1)).create(any());
    }

    @Test
    void update() {
        User user = createUser();
        NameDTO nameDTO = createNameDTO();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User userUpdated = userService.update(user.getEmail(), nameDTO);

        assertEquals(user, userUpdated);

        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        User user = createUser();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(accountRepository.findByOwner(any())).thenReturn(Optional.empty());

        assertEquals(user, userService.delete(user.getEmail()));

        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).delete(any());
        verify(accountRepository, times(1)).findByOwner(any());
    }

    @Test
    void findAll() {
        User user = createUser();
        List<User> users = List.of(user);

        Pageable pageable = PageRequest.of(0, 20);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(users, pageable, users.size()));

        assertEquals(new PageImpl<>(users, pageable, users.size()), userService.findAll(0));

        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test");

        return user;
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setName("Test");

        return userDTO;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setOwner(createUser());
        account.setId(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"));

        return account;
    }

    private NameDTO createNameDTO() {
        NameDTO nameDTO = new NameDTO();
        nameDTO.setName("Test");

        return nameDTO;
    }

}
