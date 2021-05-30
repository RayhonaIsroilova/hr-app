package ecma.ai.hrapp.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    private String username;

    @Email
    private String email;

    @NotNull
    private String position;

    @NotNull
    private Integer roleId; //3 xodim 2 manager
}
