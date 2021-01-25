package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminGroup;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultGroup;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.GroupPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.DefaultGroupRepository;
import tech.guyi.web.quick.core.exception.NoAuthorizationException;
import tech.guyi.web.quick.permission.admin.defaults.service.*;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.permission.authorization.AuthorizationCurrent;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultGroupService implements GroupService {

    @Getter
    @Resource
    private DefaultGroupRepository repository;

    @Resource
    private PermissionService permissionService;
    @Resource
    private GroupPermissionService groupPermissionService;
    @Resource
    private AdminService adminService;
    @Resource
    private AdminGroupService adminGroupService;

    @Resource
    private AuthorizationCurrent current;

    @Override
    public Class<DefaultGroup> entityClass() {
        return DefaultGroup.class;
    }

    @Override
    @DefaultAdminTransactional
    public void deleteById(String id) {
        this.groupPermissionService.deleteByGroupId(id);
        this.adminGroupService.deleteByGroupId(id);
        this.repository.deleteById(id);
    }

    @Override
    public List<DefaultPermission> getPermissions(String groupId) {
        return this.permissionService.findAllById(
                this.groupPermissionService.findByGroupId(groupId)
                        .stream()
                        .map(GroupPermission::getPermissionId)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    @DefaultAdminTransactional
    public void grant(String groupId, Set<String> permissionIds) {

        this.groupPermissionService.deleteByGroupIdAndPermissionIdIn(
                groupId,
                this.current.current(DefaultAdminEntry.class)
                        .map(DefaultAdminEntry::getId)
                        .map(this.adminService::getPermissions)
                        .map(ps -> ps.stream().map(PermissionEntry::getId).collect(Collectors.toSet()))
                        .orElseThrow(NoAuthorizationException::new)
        );

        List<GroupPermission> gps = permissionIds.stream()
                .map(permissionId -> {
                    GroupPermission gp = new GroupPermission();
                    gp.setGroupId(groupId);
                    gp.setPermissionId(permissionId);
                    this.groupPermissionService.autoTime(gp);
                    return gp;
                })
                .collect(Collectors.toList());
        this.groupPermissionService.saveAll(gps);
    }

    @Override
    public List<DefaultGroup> findByAdminId(String adminId) {
        return this.adminGroupService.findByAdminId(adminId)
                .stream()
                .map(AdminGroup::getGroupId)
                .map(groupId -> this.findById(groupId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void grandGroup(String adminId, Set<String> groupId) {
        this.adminGroupService.deleteByAdminIdAndGroupIdIn(
                adminId,
                this.current.current(DefaultAdminEntry.class)
                        .map(admin -> {
                            if (admin.isSuperAdmin()){
                                return this.findAll();
                            }else{
                                return this.findByAdminId(admin.getId());
                            }
                        })
                        .map(gs -> gs.stream().map(DefaultGroup::getId).collect(Collectors.toSet()))
                        .orElseThrow(NoAuthorizationException::new)
        );

        List<AdminGroup> ags = groupId.stream()
                .map(id -> {
                    AdminGroup ag = new AdminGroup();
                    ag.setGroupId(id);
                    ag.setAdminId(adminId);
                    this.adminGroupService.autoTime(ag);
                    return ag;
                })
                .collect(Collectors.toList());

        this.adminGroupService.saveAll(ags);
    }

}
