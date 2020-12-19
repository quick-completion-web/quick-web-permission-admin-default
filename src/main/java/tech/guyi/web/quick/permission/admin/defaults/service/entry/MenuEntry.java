package tech.guyi.web.quick.permission.admin.defaults.service.entry;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;

import java.util.List;

@Data
public class MenuEntry {

    private String id;
    private String key;
    private String name;
    private String path;
    private String icon;
    private String parent;
    private int order;
    private List<MenuEntry> children;

    public String getTitle(){
        return this.name;
    }

    public static MenuEntry from(DefaultPermission permission){
        MenuEntry entry = new MenuEntry();
        BeanUtils.copyProperties(permission,entry);
        return entry;
    }

}
