package tech.guyi.web.quick.permission.admin.defaults.db.repository;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.GroupPermission;
import tech.guyi.web.quick.service.repository.QuickRepository;

import java.util.Set;

public interface GroupPermissionRepository extends QuickRepository<GroupPermission,String> {

    void deleteByPermissionId(String permissionId);

    void deleteByGroupIdAndPermissionIdIn(String groupId, Set<String> permissionId);

    void deleteByGroupId(String groupId);

}
