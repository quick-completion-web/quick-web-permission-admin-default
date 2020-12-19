package tech.guyi.web.quick.permission.admin.defaults.service.entry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultAdmin;
import tech.guyi.web.quick.permission.admin.entry.Admin;
import tech.guyi.web.quick.permission.admin.entry.Permission;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultAdminEntry extends DefaultAdmin implements Admin {

    private boolean superAdmin;
    private Set<Permission> permissions;

}
