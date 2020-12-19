package tech.guyi.web.quick.permission.admin.defaults.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.permission.admin.defaults.controller.parameter.GrantParameter;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultAdmin;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultGroup;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.service.AdminService;
import tech.guyi.web.quick.permission.admin.defaults.service.GroupService;
import tech.guyi.web.quick.permission.annotation.Authorization;
import tech.guyi.web.quick.service.controller.QuickServiceController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("group")
public class GroupController implements QuickServiceController<DefaultGroup,String> {

    @Getter
    @Resource
    private GroupService service;
    @Resource
    private AdminService adminService;

    @GetMapping("permissions/{id}")
    @Authorization(detail = "获取管理员组权限列表")
    public ResponseEntity<ResponseContent<List<DefaultPermission>>> permissions(@PathVariable("id")String id){
        return ResponseEntities.ok(this.service.getPermissions(id));
    }

    @PostMapping("grant")
    @Authorization(detail = "管理员组授权")
    public ResponseEntity<ResponseContent<Void>> grant(@RequestBody GrantParameter parameter){
        this.service.grant(parameter.getId(), parameter.getPermissionIds());
        return ResponseEntities.ok();
    }

    @GetMapping("admin/{adminId}")
    @Authorization(detail = "获取管理员组列表")
    public ResponseEntity<ResponseContent<List<DefaultGroup>>> admin(@PathVariable("adminId")String adminId){
        return this.adminService.findById(adminId)
                .map(DefaultAdmin::getId)
                .map(this.service::findByAdminId)
                .map(ResponseEntities::ok)
                .orElseGet(ResponseEntities::_404);
    }

    @PostMapping("admin/{adminId}")
    @Authorization(detail = "分配管理员组组")
    public ResponseEntity<ResponseContent<Boolean>> admin(@PathVariable("adminId")String adminId,@RequestBody Set<String> groupId){
        return this.adminService.findById(adminId)
                .map(admin -> {
                    this.service.grandGroup(admin.getId(),groupId);
                    return ResponseEntities.ok(true);
                })
                .orElseGet(ResponseEntities::_404);
    }

}
