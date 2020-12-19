package tech.guyi.web.quick.permission.admin.defaults.service.entry;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.entry.Permission;

import java.util.List;

@Data
public class PermissionEntry implements Permission {

    private String id;
    private String key;
    private String name;
    private String path;
    private String parent;
    private String icon;
    private String method;
    private boolean menu;
    private int order;
    private List<PermissionEntry> children;

    public String getTitle(){
        return this.name;
    }

    public static PermissionEntry from(DefaultPermission permission){
        PermissionEntry entry = new PermissionEntry();
        BeanUtils.copyProperties(permission,entry);
        return entry;
    }

}
