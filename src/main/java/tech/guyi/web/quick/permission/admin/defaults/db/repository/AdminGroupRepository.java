package tech.guyi.web.quick.permission.admin.defaults.db.repository;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminGroup;
import tech.guyi.web.quick.service.repository.QuickRepository;

import java.util.Set;

public interface AdminGroupRepository extends QuickRepository<AdminGroup,String> {

    void deleteByAdminIdAndGroupIdIn(String adminId, Set<String> groupId);

    void deleteByGroupId(String groupId);

}
