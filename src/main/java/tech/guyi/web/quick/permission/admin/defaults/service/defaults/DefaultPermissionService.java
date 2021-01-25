package tech.guyi.web.quick.permission.admin.defaults.service.defaults;

import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import tech.guyi.web.quick.core.exception.WebRequestException;
import tech.guyi.web.quick.permission.admin.defaults.db.DefaultAdminTransactional;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.AdminPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.db.repository.DefaultPermissionRepository;
import tech.guyi.web.quick.permission.admin.defaults.service.PermissionService;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.MenuEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.permission.authorization.AuthorizationCurrent;
import tech.guyi.web.quick.service.entity.QuickUuidEntity;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifier;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultPermissionService implements PermissionService {

    @Getter
    @Resource
    private DefaultPermissionRepository repository;

    @Resource
    private AuthorizationCurrent current;

    @Resource
    private DefaultAdminPermissionService adminPermissionService;
    @Resource
    private DefaultGroupPermissionService groupPermissionService;

    @Override
    public Class<DefaultPermission> entityClass() {
        return DefaultPermission.class;
    }

    @Override
    public DefaultPermission autoSave(DefaultPermission entity) {
        this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("key"), entity.getKey())))
                .filter(permission -> !permission.getId().equals(entity.getId()))
                .ifPresent(permission -> {
                    throw new WebRequestException("唯一标识不能重复");
                });
        this.findOne((root,query,builder) -> builder.and(builder.equal(root.get("path"), entity.getPath())))
                .filter(permission -> !permission.getId().equals(entity.getId()))
                .ifPresent(permission -> {
                    throw new WebRequestException("路径不能重复");
                });
        this.autoTime(entity);
        this.repository.save(entity);

        this.current.current(DefaultAdminEntry.class)
                .filter(admin -> !admin.isSuperAdmin())
                .ifPresent(admin -> {
                    AdminPermission ap = new AdminPermission();
                    ap.setAdminId(admin.getId());
                    ap.setPermissionId(entity.getId());
                    this.adminPermissionService.autoSave(ap);
                });

        return entity;
    }

    @Override
    @DefaultAdminTransactional
    public void deleteById(String id) {
        this.findById(id).ifPresent(permission ->
                this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("parent"), permission.getPath())))
                        .stream()
                        .map(DefaultPermission::getId)
                        .forEach(this::deleteById));
        this.adminPermissionService.deleteByPermissionId(id);
        this.groupPermissionService.deleteByPermissionId(id);
        this.repository.deleteById(id);
    }

    @Override
    public List<MenuEntry> findMenus() {
        List<DefaultPermission> permissions = this.findAll((root,query,builder) -> builder.and(
                builder.equal(root.get("menu"), true)
        ));

        Map<String,MenuEntry> menus = permissions
                .stream()
                .collect(Collectors.toMap(DefaultPermission::getPath,MenuEntry::from));

        menus.values().stream()
                .filter(m -> !StringUtils.isEmpty(m.getParent()))
                .forEach(menu -> {
                    MenuEntry entry = menus.get(menu.getParent());
                    List<MenuEntry> children = Optional.ofNullable(entry.getChildren())
                            .orElseGet(LinkedList::new);
                    children.add(menu);
                    entry.setChildren(children);
                });

        return menus.values()
                .stream()
                .filter(m -> StringUtils.isEmpty(m.getParent()))
                .sorted(Comparator.comparingInt(MenuEntry::getOrder))
                .peek(m -> {
                    if (m.getChildren() != null){
                        m.getChildren().sort(Comparator.comparingInt(MenuEntry::getOrder));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionEntry> tree() {

        Map<String,PermissionEntry> permissions = this.findAll((root,query,builder) -> builder.and(builder.equal(root.get("menu"), false)))
                .stream()
                .collect(Collectors.toMap(DefaultPermission::getPath, PermissionEntry::from));

        permissions.values().stream()
                .filter(p -> !StringUtils.isEmpty(p.getParent()))
                .forEach(permission -> {
                    PermissionEntry entry = permissions.get(permission.getParent());
                    List<PermissionEntry> children = Optional.ofNullable(entry.getChildren())
                            .orElseGet(LinkedList::new);
                    children.add(permission);
                    entry.setChildren(children);
                });

        return permissions.values()
                .stream()
                .filter(p -> StringUtils.isEmpty(p.getParent()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean menuOrder(String currentId,String targetId) {
        Optional<DefaultPermission> current = this.findById(currentId);
        Optional<DefaultPermission> target = this.findById(targetId);

        if (!current.isPresent() || !target.isPresent()){
            throw new WebRequestException("菜单ID不存子");
        }

        if (
                (StringUtils.isEmpty(current.get().getParent()) && !StringUtils.isEmpty(target.get().getParent()))
                || (!StringUtils.isEmpty(current.get().getParent()) && !current.get().getParent().equals(target.get().getParent()))
        ){
            throw new WebRequestException("不允许跨层级排序");
        }

        List<DefaultPermission> menus = this.findAll((root,query,builder) -> builder.and(
                builder.equal(root.get("menu"), true),
                StringUtils.isEmpty(current.get().getParent()) ?
                        builder.isNull(root.get("parent")) : builder.equal(root.get("parent"), current.get().getParent())
        ), Sort.by("order"));

        DefaultPermission currentMenu = menus.stream().filter(menu -> menu.getId().equals(currentId)).findFirst().orElse(null);
        menus = menus.stream().filter(menu -> !menu.getId().equals(currentId)).collect(Collectors.toList());

        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getId().equals(targetId)){
                menus.add(i,currentMenu);
                break;
            }
        }

        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setOrder(i);
        }

        menus.forEach(this::autoSave);

        return true;
    }

}
