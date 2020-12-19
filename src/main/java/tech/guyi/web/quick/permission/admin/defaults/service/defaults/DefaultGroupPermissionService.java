package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.GroupPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.GroupPermissionRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.GroupPermissionService;

import javax.annotation.Resource;
import java.util.Set;

public class DefaultGroupPermissionService implements GroupPermissionService {

    @Getter
    @Resource
    private GroupPermissionRepository repository;

    @Override
    public Class<GroupPermission> entityClass() {
        return GroupPermission.class;
    }

    @Override
    @DefaultAdminTransactional
    public void deleteByGroupIdAndPermissionIdIn(String groupId, Set<String> permissionId) {
        this.repository.deleteByGroupIdAndPermissionIdIn(groupId, permissionId);
    }

    @Override
    public void deleteByPermissionId(String permissionId) {
        this.repository.deleteByPermissionId(permissionId);
    }

    @Override
    public void deleteByGroupId(String groupId) {
        this.repository.deleteByGroupId(groupId);
    }
}
