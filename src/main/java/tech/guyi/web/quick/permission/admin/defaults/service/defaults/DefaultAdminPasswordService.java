package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPassword;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.AdminPasswordRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.AdminPasswordService;

import javax.annotation.Resource;

public class DefaultAdminPasswordService implements AdminPasswordService {

    @Getter
    @Resource
    private AdminPasswordRepository repository;

    @Override
    public Class<AdminPassword> entityClass() {
        return AdminPassword.class;
    }

    @Override
    public void deleteByAdminId(String adminId) {
        this.repository.deleteByAdminId(adminId);
    }


}
