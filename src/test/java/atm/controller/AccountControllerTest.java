package atm.controller;

import atm.dto.BalanceDTO;
import atm.dto.ValueDTO;
import atm.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void withdrawal() throws Exception {
        when(accountService.withdrawal(any(), any())).thenReturn(createBalanceDTO());

        mockMvc.perform(put("/api/account/withdrawal/test@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValueDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("operationValue", is(10)))
                .andExpect(jsonPath("fee", is(1.5)))
                .andExpect(jsonPath("oldBalance", is(20)))
                .andExpect(jsonPath("newBalance", is(8.5)));
    }

    private ValueDTO createValueDTO() {
        ValueDTO valueDTO = new ValueDTO();
        valueDTO.setValue(BigDecimal.TEN);

        return valueDTO;
    }

    private BalanceDTO createBalanceDTO() {
        return new BalanceDTO(BigDecimal.TEN, BigDecimal.valueOf(1.5),
                BigDecimal.valueOf(20), BigDecimal.valueOf(8.5));
    }
}
