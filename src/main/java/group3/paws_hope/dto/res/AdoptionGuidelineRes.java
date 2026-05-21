package group3.paws_hope.dto.res;

import group3.paws_hope.entity.AdoptionGuideline;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AdoptionGuidelineRes {
    private Long guideId;
    private String title;
    private String content;
    private Integer priority;

    public static AdoptionGuidelineRes toJson(AdoptionGuideline adoptionGuideline) {
        return new AdoptionGuidelineRes(
                adoptionGuideline.getGuideId(),
                adoptionGuideline.getTitle(),
                adoptionGuideline.getContent(),
                adoptionGuideline.getPriority()
        );
    }
}