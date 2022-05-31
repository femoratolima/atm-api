package atm.controller;

import atm.domain.User;
import atm.dto.NameDTO;
import atm.dto.UserAccountDTO;
import atm.dto.UserDTO;
import atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {

        return userService.findById(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<User>> findAll(@RequestParam(value = "page") int page) {

        return ResponseEntity.ok(userService.findAll(page));
    }

    @PostMapping
    public ResponseEntity<UserAccountDTO> create(@Valid @RequestBody UserDTO user) {

        UserAccountDTO userAccountDTO = userService.create(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}")
                .buildAndExpand(userAccountDTO.getEmail()).toUri();

        return ResponseEntity.created(location).body(userAccountDTO);
    }

    @PutMapping(path = "/{email}")
    public ResponseEntity<User> update(@PathVariable String email, @Valid @RequestBody NameDTO name) {

        return ResponseEntity.ok(userService.update(email, name));
    }

    @DeleteMapping(path = "/{email}")
    public ResponseEntity<User> deleteById(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.delete(email));
    }
}
