package group3.paws_hope.dto.res;

import group3.paws_hope.entity.Kennel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class KennelRes {
    private Long kennelId;
    private String name;
    private Integer capacity;
    private String description;

    public static KennelRes toJson(Kennel kennel) {
        return new KennelRes(
                kennel.getKennelId(),
                kennel.getName(),
                kennel.getCapacity(),
                kennel.getDescription()
        );
    }
}