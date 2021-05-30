package ecma.ai.hrapp.entity;

import ecma.ai.hrapp.entity.enums.Month;
import ecma.ai.hrapp.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class PaidSalary extends AbsEntity {

    @ManyToOne
    private User owner;//egasi

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    private Month period;

    private boolean paid = false;

}
