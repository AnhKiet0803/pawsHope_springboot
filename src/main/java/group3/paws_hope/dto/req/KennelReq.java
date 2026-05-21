package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KennelReq {
    private Long kennelId;

    @NotBlank(message = "Kennel name cannot be left blank.")
    private String name;

    @NotNull(message = "Capacity cannot be null.")
    private Integer capacity;

    private String description;
}