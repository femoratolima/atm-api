package atm.controller;

import atm.domain.Operation;
import atm.domain.User;
import atm.enums.OperationType;
import atm.service.OperationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OperationController.class)
@AutoConfigureMockMvc
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService operationService;

    @Test
    void findAllByEmail() throws Exception {
        when(operationService.findAllByEmail(anyString(), anyInt())).thenReturn(createResponse());

        mockMvc.perform(get("/api/operation/test@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is("5fc03087-d265-11e7-b8c6-83e29cd24f4c")))
                .andExpect(jsonPath("$.content[0].operationType", is("WITHDRAWAL")))
                .andExpect(jsonPath("$.content[0].owner.email", is("test@test.com")))
                .andExpect(jsonPath("$.content[0].owner.name", is("Test")))
                .andExpect(jsonPath("$.content[0].value", is(10)))
                .andExpect(jsonPath("$.content[0].date", is("2022-01-06T12:56:00")));
    }

    private Page<Operation> createResponse() {
        User user = createUser();

        Operation operation = new Operation();
        operation.setId(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"));
        operation.setOperationType(OperationType.WITHDRAWAL);
        operation.setValue(BigDecimal.TEN);
        operation.setOwner(user);
        operation.setDate(LocalDateTime.of(2022, Month.JANUARY, 6, 12, 56));

        Pageable pageable = PageRequest.of(0, 20);

        List<Operation> operations = List.of(operation);

        return new PageImpl<>(operations, pageable, operations.size());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test");

        return user;
    }
}
