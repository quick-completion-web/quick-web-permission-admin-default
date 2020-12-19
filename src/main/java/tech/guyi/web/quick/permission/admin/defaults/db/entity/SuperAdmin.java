package tech.guyi.web.quick.permission.admin.defaults.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.guyi.web.quick.service.entity.QuickUuidEntity;

import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SuperAdmin extends QuickUuidEntity {

    private String adminId;
    private boolean superAdmin;

}
