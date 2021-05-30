package ecma.ai.hrapp.entity.template;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@Data
public class AbsEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @CreationTimestamp
    private Timestamp createdAt; //qachon qo'shildi

    @UpdateTimestamp
    private Timestamp updatedAt;//qachondr o'zgartirilsa

    @CreatedBy
    @Column(updatable = false) //o'zgartirishga ruxsat bermaydi?
    private UUID createdBy;


    @LastModifiedBy
    private UUID updatedBy; //kim tomondan oxirgi o'zgartirish kiritildi


}
