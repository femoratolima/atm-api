package atm.controller;

import atm.dto.BalanceDTO;
import atm.dto.ValueDTO;
import atm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PutMapping(path = "/withdrawal/{email}")
    public ResponseEntity<BalanceDTO> withdrawal(@PathVariable String email, @RequestBody ValueDTO valueDTO) {

        return ResponseEntity.ok(accountService.withdrawal(email, valueDTO));
    }
}
