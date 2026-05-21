package group3.paws_hope.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionGuidelineReq {

    private Long guideId;

    @NotBlank(message = "Title cannot be left blank.")
    private String title;

    @NotBlank(message = "Content cannot be left blank.")
    private String content;

    private Integer priority;
}