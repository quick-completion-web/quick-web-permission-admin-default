package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminGroup;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.AdminGroupRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.AdminGroupService;

import javax.annotation.Resource;
import java.util.Set;

public class DefaultAdminGroupService implements AdminGroupService {

    @Getter
    @Resource
    private AdminGroupRepository repository;

    @Override
    public Class<AdminGroup> entityClass() {
        return AdminGroup.class;
    }

    @Override
    @DefaultAdminTransactional
    public void deleteByAdminIdAndGroupIdIn(String adminId, Set<String> groupId) {
        this.repository.deleteByAdminIdAndGroupIdIn(adminId,groupId);
    }

    @Override
    public void deleteByGroupId(String groupId) {
        this.repository.deleteByGroupId(groupId);
    }
}
