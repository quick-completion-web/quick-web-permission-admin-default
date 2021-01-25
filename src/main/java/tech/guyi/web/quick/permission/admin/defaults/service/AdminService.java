package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultAdmin;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultGroup;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.service.service.QuickService;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifierItem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService extends QuickService<DefaultAdmin,String> {

    @Override
    default List<UniquenessVerifierItem<DefaultAdmin>> verifiers() {
        return Arrays.asList(
                new UniquenessVerifierItem<>(
                        "名称不能重复",
                        (root,query,builder,entity) -> builder.and(builder.equal(root.get("name"), entity.getName()))
                ),
                new UniquenessVerifierItem<>(
                        "登录名称不能重复",
                        (root,query,builder,entity) -> builder.and(builder.equal(root.get("loginName"), entity.getLoginName()))
                )
        );
    }

    void initAdmin(DefaultAdmin admin, String password, Boolean superAdmin);

    default Optional<DefaultAdmin> findByLoginName(String loginName){
        return this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("loginName"),loginName)));
    }

    Optional<DefaultAdmin> login(String loginName,String password);

    boolean setPassword(DefaultAdmin admin,String password);

    Set<PermissionEntry> getPermissions(DefaultAdmin admin);

    Set<PermissionEntry> getPermissions(String adminId);

    List<DefaultGroup> getGroups(DefaultAdmin admin);

    DefaultAdminEntry getEntry(DefaultAdmin admin);

    DefaultAdmin autoSave(DefaultAdminEntry entry, Boolean superAdmin);

    void grant(String adminId, Set<String> permissionIds);
}
