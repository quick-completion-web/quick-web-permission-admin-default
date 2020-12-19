package tech.guyi.web.quick.permission.admin.defaults.db.repository;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPassword;
import tech.guyi.web.quick.service.repository.QuickRepository;

public interface AdminPasswordRepository extends QuickRepository<AdminPassword,String > {

    void deleteByAdminId(String adminId);

}
