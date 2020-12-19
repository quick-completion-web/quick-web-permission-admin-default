package tech.guyi.web.quick.permission.admin.defaults.db.entity;

import lombok.Data;
import tech.guyi.web.quick.permission.admin.defaults.db.entry.EntityAttach;
import tech.guyi.web.quick.permission.admin.entry.Permission;
import tech.guyi.web.quick.service.entity.QuickUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Data
@Entity
public class DefaultPermission extends QuickUuidEntity implements Permission {

    private String key;
    private String name;
    private String path;
    private String method;
    private String icon;
    private String parent;
    private boolean menu;
    @Column(name = "menu_order")
    private int order;
    @Column(columnDefinition = "mediumtext")
    private EntityAttach attach;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultPermission that = (DefaultPermission) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key);
    }
}
