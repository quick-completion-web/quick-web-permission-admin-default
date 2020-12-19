package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.AdminPermissionRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.AdminPermissionService;

import javax.annotation.Resource;
import java.util.Set;

public class DefaultAdminPermissionService implements AdminPermissionService {

    @Getter
    @Resource
    private AdminPermissionRepository repository;

    @Override
    public Class<AdminPermission> entityClass() {
        return AdminPermission.class;
    }

    @Override
    public void deleteByPermissionId(String permissionId) {
        this.repository.deleteByPermissionId(permissionId);
    }

    @Override
    @DefaultAdminTransactional
    public void deleteByAdminIdAndPermissionIdIn(String adminId, Set<String> permissionId) {
        this.repository.deleteByAdminIdAndPermissionIdIn(adminId, permissionId);
    }
}
