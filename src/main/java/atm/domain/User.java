package atm.domain;

import atm.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @Email(message = "Email should be valid!")
    private String email;

    private String name;

    public static User fromDto(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());

        return user;
    }
}
