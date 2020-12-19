package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.SuperAdmin;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.Optional;

public interface SuperAdminService extends QuickService<SuperAdmin,String> {

    default Optional<SuperAdmin> findByAdminId(String adminId){
        return this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("adminId"), adminId)));
    }

}
