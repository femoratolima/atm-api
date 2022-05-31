package atm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum WithdrawalFee {

    THREE_PERCENT(BigDecimal.ONE, new BigDecimal("100.99"), new BigDecimal("3")),
    TWO_PERCENT(new BigDecimal("101"), new BigDecimal("250.99"), new BigDecimal("2")),
    ONE_PERCENT(new BigDecimal("251"), new BigDecimal("300"), new BigDecimal("1"));

    private final BigDecimal minValue;
    private final BigDecimal maxValue;
    private final BigDecimal fee;

    public static Stream<WithdrawalFee> stream() {
        return Stream.of(WithdrawalFee.values());
    }
}
