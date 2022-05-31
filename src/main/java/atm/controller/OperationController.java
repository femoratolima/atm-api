package atm.controller;


import atm.domain.Operation;
import atm.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping(path = "/{email}")
    public ResponseEntity<Page<Operation>> findAllByEmail(@RequestParam(value = "page") int page, @PathVariable String email) {

        return ResponseEntity.ok(operationService.findAllByEmail(email, page));
    }
}
