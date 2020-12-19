package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPassword;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.Optional;

public interface AdminPasswordService extends QuickService<AdminPassword,String> {

    default Optional<AdminPassword> findByAdminIdAndPassword(String adminId, String password){
        return this.findOne((root,query,builder) -> builder.and(
                builder.equal(root.get("adminId"),adminId),
                builder.equal(root.get("password"),password)
        ));
    }

    default Optional<AdminPassword> findByAdminId(String adminId){
        return this.findOne((root,query,builder) -> builder.and(
                builder.equal(root.get("adminId"),adminId)
        ));
    }

    void deleteByAdminId(String adminId);

}
