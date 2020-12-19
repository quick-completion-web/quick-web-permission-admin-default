package tech.guyi.web.quick.permission.admin.defaults.controller.parameter;

import lombok.Data;

import java.util.Set;

@Data
public class GrantParameter {

    private String id;
    private Set<String> permissionIds;

}
