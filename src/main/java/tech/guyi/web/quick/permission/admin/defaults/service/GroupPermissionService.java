package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.GroupPermission;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.List;
import java.util.Set;

public interface GroupPermissionService extends QuickService<GroupPermission,String> {

    default List<GroupPermission> findByGroupId(String groupId){
        return this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("groupId"),groupId)));
    }

    void deleteByGroupIdAndPermissionIdIn(String groupId, Set<String> permissionId);

    void deleteByPermissionId(String permissionId);

    void deleteByGroupId(String groupId);

}
