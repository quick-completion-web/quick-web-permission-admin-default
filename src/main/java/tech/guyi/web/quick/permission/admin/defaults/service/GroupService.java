package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultGroup;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.service.service.QuickService;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifierItem;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface GroupService extends QuickService<DefaultGroup,String> {

    @Override
    default List<UniquenessVerifierItem<DefaultGroup>> verifiers() {
        return Collections.singletonList(new UniquenessVerifierItem<>(
                "名称不能重复",
                (root,query,builder,entity) -> builder.and(builder.equal(root.get("name"),entity.getName()))
        ));
    }

    List<DefaultPermission> getPermissions(String groupId);

    void grant(String groupId, Set<String> permissionIds);

    List<DefaultGroup> findByAdminId(String adminId);

    void grandGroup(String adminId,Set<String> groupId);

    default List<DefaultGroup> findDefaultGroup(){
        return this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("defaults"), true)));
    }

}
