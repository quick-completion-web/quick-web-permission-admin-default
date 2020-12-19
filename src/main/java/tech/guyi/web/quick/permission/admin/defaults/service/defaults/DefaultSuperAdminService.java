package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.SuperAdmin;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.SuperAdminRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.SuperAdminService;

import javax.annotation.Resource;

public class DefaultSuperAdminService implements SuperAdminService {

    @Getter
    @Resource
    private SuperAdminRepository repository;

    @Override
    public Class<SuperAdmin> entityClass() {
        return SuperAdmin.class;
    }

}
