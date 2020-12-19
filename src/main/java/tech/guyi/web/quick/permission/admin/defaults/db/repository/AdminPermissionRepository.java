package tech.guyi.web.quick.permission.admin.defaults.db.repository;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPermission;
import tech.guyi.web.quick.service.repository.QuickRepository;

import java.util.Set;

public interface AdminPermissionRepository extends QuickRepository<AdminPermission,String> {

    void deleteByPermissionId(String permissionId);

    void deleteByAdminIdAndPermissionIdIn(String adminId, Set<String> permissionId);

}
