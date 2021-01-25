package tech.guyi.web.quick.permission.admin.defaults.controller;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.core.exception.WebRequestException;
import tech.guyi.web.quick.permission.admin.defaults.controller.parameter.GrantParameter;
import tech.guyi.web.quick.permission.admin.defaults.controller.parameter.InitAdminParameter;
import tech.guyi.web.quick.permission.admin.defaults.controller.parameter.LoginParameter;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultAdmin;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultGroup;
import tech.guyi.web.quick.core.exception.NoAuthorizationException;
import tech.guyi.web.quick.permission.admin.defaults.service.AdminService;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.DefaultAdminEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.permission.admin.entry.Admin;
import tech.guyi.web.quick.permission.admin.entry.Permission;
import tech.guyi.web.quick.permission.annotation.Authorization;
import tech.guyi.web.quick.permission.authorization.AuthorizationCurrent;
import tech.guyi.web.quick.permission.authorization.memory.AuthorizationInfoMemorySupplier;
import tech.guyi.web.quick.service.controller.QuickServiceController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("admin")
public class AdminController implements QuickServiceController<DefaultAdmin,String> {

    @Getter
    @Resource
    private AdminService service;

    @Resource
    private AuthorizationCurrent current;
    @Resource
    private AuthorizationInfoMemorySupplier memorySupplier;

    @Override
    public void onDelete(String id) {
        this.current.current(DefaultAdminEntry.class)
                .map(DefaultAdminEntry::getId)
                .filter(adminId -> !adminId.equals(id))
                .orElseThrow(() -> new WebRequestException("不能删除自己"));
    }

    @PostMapping("init")
    @Authorization(detail = "创建管理员")
    public ResponseEntity<ResponseContent<Void>> initAdmin(@RequestBody InitAdminParameter parameter){
        DefaultAdmin admin = new DefaultAdmin();
        BeanUtils.copyProperties(parameter,admin);
        Boolean superAdmin = this.current.current(DefaultAdminEntry.class)
                .filter(DefaultAdminEntry::isSuperAdmin)
                .map(current -> parameter.isSuperAdmin())
                .orElse(null);
        this.service.initAdmin(admin, parameter.getPassword(),superAdmin);
        return ResponseEntities.ok();
    }

    @GetMapping("entry/{id}")
    @Authorization(detail = "获取管理员实体")
    public ResponseEntity<ResponseContent<DefaultAdminEntry>> getEntry(@PathVariable("id") String id){
        return this.service.findById(id)
                .map(this.service::getEntry)
                .map(ResponseEntities::ok)
                .orElseGet(ResponseEntities::_404);
    }

    @PostMapping("save")
    @Authorization(detail = "保存管理员信息")
    public ResponseEntity<ResponseContent<DefaultAdmin>> save(@RequestBody DefaultAdminEntry entry){
        if (this.existsById(entry.getId())){
            this.onUpdate(entry);
        }else{
            this.onCreate(entry);
        }
        Boolean superAdmin = this.current.current(DefaultAdminEntry.class)
                .filter(DefaultAdminEntry::isSuperAdmin)
                .map(current -> entry.isSuperAdmin())
                .orElse(null);
        return Optional.ofNullable(this.service.autoSave(entry,superAdmin))
                .map(ResponseEntities::ok)
                .orElseGet(ResponseEntities::fail);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseContent<String>> login(@RequestBody LoginParameter login){
        return this.service.login(login.getLoginName(),login.getPassword())
                .map(service::getEntry)
                .map(memorySupplier.getMemory()::save)
                .map(token -> ResponseEntities.ok("登录成功",token))
                .orElseGet(() -> ResponseEntities.fail("登录名或密码错误"));
    }

    @GetMapping("logout")
    @Authorization(detail = "退出登录")
    public ResponseEntity<ResponseContent<Void>> logout(){
        this.current.currentKey()
                .ifPresent(this.memorySupplier.getMemory()::remove);
        return ResponseEntities.ok();
    }

    @PostMapping("password/set")
    @Authorization(detail = "重置管理员密码")
    public ResponseEntity<ResponseContent<Boolean>> setPassword(String adminId,String password){
        return this.service.findById(adminId)
                .map(admin -> service.setPassword(admin,password))
                .map(ResponseEntities::ok)
                .orElseGet(ResponseEntities::_404);
    }

    @PostMapping("password/change")
    @Authorization(detail = "修改密码")
    public ResponseEntity<ResponseContent<Boolean>> changePassword(String password){
        return this.current.current(DefaultAdminEntry.class)
                .map(DefaultAdminEntry::getId)
                .flatMap(this.service::findById)
                .map(admin -> service.setPassword(admin,password))
                .map(ResponseEntities::ok)
                .orElseThrow(NoAuthorizationException::new);
    }

    @GetMapping("current")
    @Authorization(detail = "获取当前登录管理员")
    public ResponseEntity<ResponseContent<DefaultAdminEntry>> current(){
        return this.current.current(DefaultAdminEntry.class)
                .map(ResponseEntities::ok)
                .orElseThrow(NoAuthorizationException::new);
    }

    @GetMapping("current/permissions")
    @Authorization(detail = "获取当前登录管理员权限列表")
    public ResponseEntity<ResponseContent<Set<PermissionEntry>>> permissions(){
        return this.current.current(DefaultAdminEntry.class)
                .map(this.service::getPermissions)
                .map(ResponseEntities::ok)
                .orElseThrow(NoAuthorizationException::new);
    }

    @GetMapping("current/menus")
    @Authorization(detail = "获取当前登录管理员菜单列表")
    public ResponseEntity<ResponseContent<Set<Permission>>> menus(){
        return this.current.current(DefaultAdminEntry.class)
                .map(Admin::getMenus)
                .map(ResponseEntities::ok)
                .orElseThrow(NoAuthorizationException::new);
    }

    @GetMapping("current/groups")
    @Authorization(detail = "获取当前登录管理员组列表")
    public ResponseEntity<ResponseContent<List<DefaultGroup>>> groups(){
        return this.current.current(DefaultAdminEntry.class)
                .map(this.service::getGroups)
                .map(ResponseEntities::ok)
                .orElseThrow(NoAuthorizationException::new);
    }

    @GetMapping("permissions/{id}")
    @Authorization(detail = "获取管理员权限列表")
    public ResponseEntity<ResponseContent<Set<PermissionEntry>>> permissions(@PathVariable("id")String id){
        return this.service.findById(id)
                .map(DefaultAdmin::getId)
                .map(this.service::getPermissions)
                .map(ResponseEntities::ok)
                .orElseGet(ResponseEntities::_404);
    }

    @PostMapping("grant")
    @Authorization(detail = "管理员授权")
    public ResponseEntity<ResponseContent<Void>> grant(@RequestBody GrantParameter parameter){
        this.service.grant(parameter.getId(), parameter.getPermissionIds());
        return ResponseEntities.ok();
    }

}
