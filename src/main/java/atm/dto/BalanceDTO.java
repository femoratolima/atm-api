package atm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BalanceDTO {

    private BigDecimal operationValue;
    private BigDecimal fee;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;

}
