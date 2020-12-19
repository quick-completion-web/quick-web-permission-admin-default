package tech.guyi.web.quick.permission.admin.defaults.controller.parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;

@Data
@EqualsAndHashCode(callSuper = true)
public class InitAdminParameter extends DefaultAdminEntry {

    private String password;

}
