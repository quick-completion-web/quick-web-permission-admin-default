package tech.guyi.web.quick.permission.admin.defaults.db.repository;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.service.repository.QuickRepository;

public interface DefaultPermissionRepository extends QuickRepository<DefaultPermission,String> {

    void deleteByParent(String parent);

}
