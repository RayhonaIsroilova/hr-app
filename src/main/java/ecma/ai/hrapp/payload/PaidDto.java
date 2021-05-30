package ecma.ai.hrapp.payload;

import ecma.ai.hrapp.entity.enums.Month;
import lombok.Data;

import java.util.UUID;
@Data
public class PaidDto {
    private double amount;
    private Month period;
    private boolean paid;
    private UUID userId;
}
