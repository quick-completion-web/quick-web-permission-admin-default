package tech.guyi.web.quick.permission.admin.defaults.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.permission.admin.defaults.controller.parameter.MenuOrderParameter;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.service.PermissionService;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.MenuEntry;
import tech.guyi.web.quick.permission.admin.defaults.service.entry.PermissionEntry;
import tech.guyi.web.quick.permission.annotation.Authorization;
import tech.guyi.web.quick.service.controller.QuickServiceController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("permission")
public class PermissionController implements QuickServiceController<DefaultPermission, String> {

    @Getter
    @Resource
    private PermissionService service;

    @GetMapping("menus")
    @Authorization(detail = "获取菜单列表")
    public ResponseEntity<ResponseContent<List<MenuEntry>>> menus(){
        return ResponseEntities.ok(this.service.findMenus());
    }

    @GetMapping("tree")
    @Authorization(detail = "获取权限树")
    public ResponseEntity<ResponseContent<List<PermissionEntry>>> tree(){
        return ResponseEntities.ok(this.service.tree());
    }

    @PostMapping("menu/order")
    @Authorization(detail = "菜单排序")
    public ResponseEntity<ResponseContent<Void>> menuOrder(@RequestBody MenuOrderParameter parameter){
        boolean success = this.service.menuOrder(parameter.getCurrentId(), parameter.getTargetId());
        return success ? ResponseEntities.ok() : ResponseEntities.fail();
    }

}
