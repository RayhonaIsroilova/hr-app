package ecma.ai.hrapp.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class TurniketDto {
    private Integer companyId;
    private UUID ownerId;
    boolean enable = true;
}
