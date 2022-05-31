package atm.domain;

import atm.converter.OperationTypeConverter;
import atm.enums.OperationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue
    private UUID id;

    @Convert(converter = OperationTypeConverter.class)
    private OperationType operationType;

    @OneToOne
    @JoinColumn(name = "email")
    private User owner;

    private BigDecimal value;

    private BigDecimal fee;

    private BigDecimal accountBalance;

    private LocalDateTime date;
}
