package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.MenuEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.service.service.QuickService;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifierItem;

import java.util.Arrays;
import java.util.List;

public interface PermissionService extends QuickService<DefaultPermission,String> {

    @Override
    default List<UniquenessVerifierItem<DefaultPermission>> verifiers() {
        return Arrays.asList(
                new UniquenessVerifierItem<>(
                        "key",
                        (root, query, builder, entity) -> builder.and(builder.equal(root.get("key"), entity.getKey()))
                ),
                new UniquenessVerifierItem<>(
                        "key",
                        (root, query, builder, entity) -> builder.and(builder.equal(root.get("path"), entity.getPath()))
                )
        );
    }

    List<MenuEntry> findMenus();

    List<PermissionEntry> tree();

    boolean menuOrder(String currentId, String targetId);

    default boolean existsByPath(String path) {
        return this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("path"), path))).isPresent();
    }
}
