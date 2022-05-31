package atm.service;

import atm.repository.AccountRepository;
import atm.repository.OperationRepository;
import atm.repository.UserRepository;
import atm.domain.Account;
import atm.domain.User;
import atm.dto.NameDTO;
import atm.dto.UserAccountDTO;
import atm.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
public class UserService {

    private static final String EMAIL_NOT_FOUND = "User with email %s not found!";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    public Optional<User> findById(String email) {
        return userRepository.findById(email);
    }

    public UserAccountDTO create(UserDTO userDTO) {
        userRepository.findById(userDTO.getEmail()).ifPresent(userFound -> {
            throw new RestClientException(String.format("Email %s already in use!", userDTO.getEmail()));
        });

        User newUser = userRepository.save(User.fromDto(userDTO));
        Account newAccount = accountService.create(newUser.getEmail());

        return UserAccountDTO.from(newUser, newAccount);
    }

    public User update(String email, NameDTO nameDTO) {
        return userRepository.findById(email).map(userFound -> {
            userFound.setName(nameDTO.getName());
            return userRepository.save(userFound);
        }).orElseThrow(() -> new RestClientException(String.format(EMAIL_NOT_FOUND, email)));
    }

    public User delete(String email) {
        return userRepository.findById(email).map(userFound -> {

            operationRepository.deleteAll(operationRepository.findAllByOwnerEmail(email));
            accountRepository.findByOwner(userFound).ifPresent(account ->  accountRepository.delete(account));
            userRepository.delete(userFound);

            return userFound;
        }).orElseThrow(() -> new RestClientException(String.format(EMAIL_NOT_FOUND, email)));
    }

    public Page<User> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 20);

        return userRepository.findAll(pageable);
    }
}
