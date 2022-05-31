package atm.converter;

import atm.enums.OperationType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class OperationTypeConverter implements AttributeConverter<OperationType, Long> {

    @Override
    public Long convertToDatabaseColumn(OperationType attribute) {
        return Objects.isNull(attribute) ? null : attribute.getValue();
    }

    @Override
    public OperationType convertToEntityAttribute(Long dbData) {
        return OperationType.fromValue(dbData);
    }
}