package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPermission;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.List;
import java.util.Set;

public interface AdminPermissionService extends QuickService<AdminPermission,String> {

    default List<AdminPermission> findByAdminId(String adminId){
        return this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("adminId"),adminId)));
    }

    void deleteByPermissionId(String permissionId);

    void deleteByAdminIdAndPermissionIdIn(String adminId, Set<String> permissionId);

}
