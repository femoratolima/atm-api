package atm.service;

import atm.domain.Operation;
import atm.domain.User;
import atm.enums.OperationType;
import atm.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Autowired
    @InjectMocks
    private OperationService operationService;

    @Mock
    private OperationRepository operationRepository;

    @Test
    void findAll() {
        Operation operation = new Operation();
        operation.setId(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"));
        operation.setOperationType(OperationType.WITHDRAWAL);
        operation.setValue(BigDecimal.TEN);
        operation.setOwner(createUser());
        operation.setDate(LocalDateTime.of(2022, Month.JANUARY, 6, 12, 56));
        List<Operation> operations = List.of(operation);

        Pageable pageable = PageRequest.of(0, 20);

        when(operationRepository.findAllByOwnerEmail(any())).thenReturn(List.of(operation));

        assertEquals(new PageImpl<>(operations, pageable, operations.size()), operationService.findAllByEmail("test", 0));

        verify(operationRepository, times(1)).findAllByOwnerEmail(any());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test");

        return user;
    }
}
