package group3.paws_hope.dto.res;

import group3.paws_hope.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserRes {
    private Long userId;
    private String username;
    private String full_name;
    private String email;
    private String phone;
    private Boolean status;

    public static UserRes toJson(User user){
        return new UserRes(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus()
        );
    }
}