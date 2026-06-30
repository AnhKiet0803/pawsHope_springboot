package group3.paws_hope.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactMessageReq {
    private String name;
    private String email;
    private String subject;
    private String message;
}