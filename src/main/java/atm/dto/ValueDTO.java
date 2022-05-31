package atm.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ValueDTO {

    @NonNull
    private BigDecimal value;
}
