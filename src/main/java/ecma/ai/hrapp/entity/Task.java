package ecma.ai.hrapp.entity;

import ecma.ai.hrapp.entity.enums.TaskStatus;
import ecma.ai.hrapp.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private Timestamp deadline;

    @ManyToOne(optional = false)
    private User taskGiver; //task beruvchi

    @ManyToOne(optional = false)
    private User taskTaker;//qabul qiluvchi

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Timestamp completedDate;


}
