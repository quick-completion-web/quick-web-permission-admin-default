package tech.guyi.web.quick.permission.admin.defaults.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.guyi.web.quick.permission.admin.defaults.db.entry.EntityAttach;
import tech.guyi.web.quick.permission.authorization.AuthorizationInfo;
import tech.guyi.web.quick.service.entity.QuickUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DefaultAdmin extends QuickUuidEntity implements AuthorizationInfo {

    private String name;
    private String loginName;
    private String avatar;
    @Column(columnDefinition = "mediumtext")
    private EntityAttach attach;

}
