package atm.enums;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestClientException;

import java.util.stream.Stream;

@AllArgsConstructor
public enum OperationType {

    WITHDRAWAL(1L, "Withdrawal");

    private final Long code;
    private final String description;

    public static OperationType fromValue(Long code) {
        return Stream.of(values())
                .filter(t -> t.getValue().equals(code))
                .findFirst()
                .orElseThrow(
                        () -> new RestClientException(
                                String.format("OperationType for code '%d' not found.", code)));
    }

    public Long getValue() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}

