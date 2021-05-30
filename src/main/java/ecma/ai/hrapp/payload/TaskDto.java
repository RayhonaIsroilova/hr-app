package ecma.ai.hrapp.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TaskDto {
    private String name;
    private String description;
    private Timestamp deadline;
    private UUID taskGiver;
    private UUID taskTaker;
    private Integer taskStatusID; //1-boshlanish 2 davom .... 3-end
    private Timestamp completedDate;


}
