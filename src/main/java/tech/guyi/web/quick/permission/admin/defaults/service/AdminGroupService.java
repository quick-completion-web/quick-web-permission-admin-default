package tech.guyi.web.quick.permission.admin.defaults.service;

import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminGroup;
import tech.guyi.web.quick.service.service.QuickService;

import java.util.List;
import java.util.Set;

public interface AdminGroupService extends QuickService<AdminGroup,String> {

    default List<AdminGroup> findByAdminId(String adminId) {
        return this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("adminId"),adminId)));
    }

    void deleteByAdminIdAndGroupIdIn(String adminId,Set<String> groupId);

    void deleteByGroupId(String groupId);
}
