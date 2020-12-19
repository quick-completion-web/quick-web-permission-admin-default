package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.MenuEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.List;

public interface PermissionService extends QuickService<DefaultPermission,String> {

    List<MenuEntry> findMenus();

    List<PermissionEntry> tree();

    boolean menuOrder(String currentId, String targetId);

    default boolean existsByPath(String path) {
        return this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("path"), path))).isPresent();
    }
}
