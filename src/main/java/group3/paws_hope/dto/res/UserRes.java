package group3.paws_hope.dto.res;

import group3.paws_hope.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Setter
@Getter
public class UserRes {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Boolean status;
    private String role;
    private Timestamp createdAt;

    public static UserRes toJson(User user){
        return new UserRes(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}