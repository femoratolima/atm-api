package atm.service;

import atm.repository.OperationRepository;
import atm.domain.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public Page<Operation> findAllByEmail(String email, int page) {
        Pageable pageable = PageRequest.of(page, 20);

        List<Operation> operations = operationRepository.findAllByOwnerEmail(email);

        return new PageImpl<>(operations, pageable, operations.size());
    }
}
