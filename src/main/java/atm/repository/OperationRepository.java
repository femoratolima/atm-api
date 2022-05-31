package atm.repository;

import atm.domain.Operation;
import atm.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID> {

    List<Operation> findAllByOwnerEmail(String email);

    @Query("SELECT op FROM Operation op WHERE op.operationType = :operationType " +
            "AND op.owner.email = :email " +
            "AND op.date BETWEEN :yesterday AND :date")
    List<Operation> findAllWithdrawalByDateAndOwnerEmail(@Param("operationType") OperationType operationType,
                                                         @Param("email") String email,
                                                         @Param("date") LocalDateTime date,
                                                         @Param("yesterday") LocalDateTime yesterday);
}
