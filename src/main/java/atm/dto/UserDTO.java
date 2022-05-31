package atm.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserDTO {

    @Email(message = "Email should be valid!")
    private String email;
    private String name;

}
