package tech.guyi.web.quick.permission.admin.defaults.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.guyi.web.quick.permission.admin.defaults.db.entry.EntityAttach;
import tech.guyi.web.quick.service.entity.QuickUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DefaultGroup extends QuickUuidEntity {

    private String name;
    private Boolean defaults;
    @Column(columnDefinition = "mediumtext")
    private EntityAttach attach;

}
