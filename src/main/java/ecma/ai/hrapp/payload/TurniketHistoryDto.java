package ecma.ai.hrapp.payload;

import ecma.ai.hrapp.entity.enums.TurniketType;
import lombok.Data;

import java.util.UUID;

@Data
public class TurniketHistoryDto {
    private UUID turniketId;
    private TurniketType type;
    boolean successful;
    private Integer companyID;
}
