package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import tech.guyi.web.quick.core.exception.WebRequestException;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.*;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.DefaultAdminRepository;
import tech.guyi.web.quick.permission.admin.defaults.encryption.AdminPasswordEncryption;
import tech.guyi.web.quick.core.exception.NoAuthorizationException;
import tech.guyi.web.quick.permission.admin.defaults.service.*;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.permission.admin.entry.Permission;
import tech.guyi.web.quick.permission.authorization.AuthorizationCurrent;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultAdminService implements AdminService {

    @Getter
    @Resource
    private DefaultAdminRepository repository;
    @Resource
    private SuperAdminService superAdminService;
    @Resource
    private AdminPasswordService passwordService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminPermissionService adminPermissionService;
    @Resource
    private AdminGroupService adminGroupService;
    @Resource
    private GroupService groupService;

    @Resource
    private AdminPasswordEncryption encryption;
    @Resource
    private AuthorizationCurrent current;

    @Override
    public Class<DefaultAdmin> entityClass() {
        return DefaultAdmin.class;
    }

    @PostConstruct
    public void onStarted(){
        if (this.count() == 0){
            DefaultAdmin admin = new DefaultAdmin();
            admin.setName("默认超级管理员");
            admin.setLoginName("admin");
            this.autoSave(admin);
            this.setPassword(admin,"admin123");

            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setAdminId(admin.getId());
            superAdmin.setSuperAdmin(true);
            this.superAdminService.autoSave(superAdmin);
        }

        if (this.permissionService.count((root,query,build) -> build.and(build.equal(root.get("menu"), true))) == 0){
            DefaultPermission permission = new DefaultPermission();
            permission.setMenu(true);
            permission.setPath("/system/menu");
            permission.setKey("default-menu-manager");
            this.permissionService.autoSave(permission);
        }
    }

    public void initAdmin(DefaultAdmin admin, String password, Boolean superAdmin){
        this.autoSave(admin);
        AdminPassword pwd = new AdminPassword();
        pwd.setAdminId(admin.getId());
        pwd.setPassword(encryption.encryption(password));
        this.passwordService.autoSave(pwd);

        if (superAdmin != null){
            SuperAdmin superAdminEntity = this.superAdminService.findByAdminId(admin.getId())
                    .orElseGet(() -> {
                        SuperAdmin tmp = new SuperAdmin();
                        tmp.setAdminId(admin.getId());
                        return tmp;
                    });
            superAdminEntity.setSuperAdmin(superAdmin);
            this.superAdminService.autoSave(superAdminEntity);
        }

        Set<String> groupId = this.groupService.findDefaultGroup()
                .stream()
                .map(DefaultGroup::getId)
                .collect(Collectors.toSet());
        this.groupService.grandGroup(admin.getId(),groupId);

    }

    @Override
    public Optional<DefaultAdmin> login(String loginName, String password) {
        return this.findByLoginName(loginName)
                .flatMap(admin -> passwordService.findByAdminIdAndPassword(admin.getId(), encryption.encryption(password))
                        .map(ps -> admin)
                );
    }

    @Override
    @DefaultAdminTransactional
    public boolean setPassword(DefaultAdmin admin, String password) {
        this.passwordService.deleteByAdminId(admin.getId());
        AdminPassword ap = new AdminPassword();
        ap.setAdminId(admin.getId());
        ap.setPassword(encryption.encryption(password));
        this.passwordService.autoSave(ap);
        return true;
    }

    @Override
    public Set<PermissionEntry> getPermissions(DefaultAdmin admin) {
        return this.superAdminService.findByAdminId(admin.getId())
                .filter(SuperAdmin::isSuperAdmin)
                .map(superAdmin -> this.permissionService.findAll())
                .map(HashSet::new)
                .orElseGet(() -> {
                    HashSet<DefaultPermission> ps = new HashSet<>(
                            this.permissionService
                                    .findAllById(this.adminPermissionService.findByAdminId(admin.getId())
                                            .stream()
                                            .map(AdminPermission::getPermissionId)
                                            .collect(Collectors.toSet())
                                    )
                    );
                    ps.addAll(
                            this.adminGroupService.findByAdminId(admin.getId())
                                    .stream()
                                    .map(AdminGroup::getGroupId)
                                    .map(this.groupService::getPermissions)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toSet())
                    );
                    return ps;
                })
                .stream()
                .map(PermissionEntry::from)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PermissionEntry> getPermissions(String adminId) {
        return this.superAdminService.findByAdminId(adminId)
                .filter(SuperAdmin::isSuperAdmin)
                .map(superAdmin -> this.permissionService.findAll())
                .map(HashSet::new)
                .orElseGet(() -> new HashSet<>(this.permissionService
                        .findAllById(this.adminPermissionService.findByAdminId(adminId)
                                .stream()
                                .map(AdminPermission::getPermissionId)
                                .collect(Collectors.toSet())
                        )))
                .stream()
                .map(PermissionEntry::from)
                .collect(Collectors.toSet());
    }

    @Override
    public List<DefaultGroup> getGroups(DefaultAdmin admin) {
        return this.superAdminService.findByAdminId(admin.getId())
                .filter(SuperAdmin::isSuperAdmin)
                .map(superAdmin -> this.groupService.findAll())
                .orElseGet(() -> this.adminGroupService.findByAdminId(admin.getId())
                        .stream()
                        .map(AdminGroup::getGroupId)
                        .map(groupId -> this.groupService.findById(groupId).orElse(null))
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList()));
    }

    @Override
    public DefaultAdminEntry getEntry(DefaultAdmin admin) {
        DefaultAdminEntry entry = new DefaultAdminEntry();
        BeanUtils.copyProperties(admin,entry);

        this.superAdminService.findByAdminId(admin.getId())
                .ifPresent(superAdmin -> entry.setSuperAdmin(superAdmin.isSuperAdmin()));

        entry.setPermissions(this.getPermissions(admin)
                .stream()
                .map(permission -> (Permission) permission)
                .collect(Collectors.toSet())
        );
        return entry;
    }

    @Override
    public DefaultAdmin autoSave(DefaultAdminEntry entry, Boolean superAdmin) {
        DefaultAdmin admin = new DefaultAdmin();
        BeanUtils.copyProperties(entry,admin);
        this.autoSave(admin);

        if (superAdmin != null){
            SuperAdmin superAdminEntity = this.superAdminService.findByAdminId(admin.getId())
                    .orElseGet(() -> {
                        SuperAdmin tmp = new SuperAdmin();
                        tmp.setAdminId(admin.getId());
                        return tmp;
                    });
            superAdminEntity.setSuperAdmin(entry.isSuperAdmin());
            this.superAdminService.autoSave(superAdminEntity);
        }

        return admin;
    }

    @Override
    @DefaultAdminTransactional
    public void grant(String adminId, Set<String> permissionIds) {
        this.adminPermissionService.deleteByAdminIdAndPermissionIdIn(
                adminId,
                this.current.current(DefaultAdminEntry.class)
                        .map(admin -> {
                            if (admin.isSuperAdmin()){
                                return this.permissionService.findAll();
                            }else{
                                return this.getPermissions(admin.getId());
                            }
                        })
                        .map(ps -> ps.stream().map(Permission::getId).collect(Collectors.toSet()))
                        .orElseThrow(NoAuthorizationException::new)
        );

        List<AdminPermission> aps = permissionIds.stream()
                .map(permissionId -> {
                    AdminPermission ap = new AdminPermission();
                    ap.setAdminId(adminId);
                    ap.setPermissionId(permissionId);
                    this.adminPermissionService.autoTime(ap);
                    return ap;
                })
                .collect(Collectors.toList());
        this.adminPermissionService.saveAll(aps);
    }

}
