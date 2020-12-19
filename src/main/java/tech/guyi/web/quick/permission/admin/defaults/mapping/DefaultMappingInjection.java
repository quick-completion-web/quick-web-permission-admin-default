package tech.guyi.web.quick.permission.admin.defaults.mapping;

import tech.guyi.web.quick.permission.admin.defaults.configuration.DefaultAdminConfiguration;
import tech.guyi.web.quick.permission.admin.defaults.db.entity.DefaultPermission;
import tech.guyi.web.quick.permission.admin.defaults.service.PermissionService;
import tech.guyi.web.quick.permission.mapping.MappingInjection;
import tech.guyi.web.quick.permission.mapping.MappingMatcher;
import tech.guyi.web.quick.permission.mapping.entry.Mapping;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DefaultMappingInjection implements MappingInjection {

    @Resource
    private MappingMatcher matcher;
    @Resource
    private DefaultAdminConfiguration configuration;

    @Resource
    private PermissionService service;

    @Override
    public void injection(List<Mapping> mappings) {
        if (configuration.isImportPermission()){
            mappings.stream()
                    .sorted(Comparator.comparing(Mapping::getUrl))
                    .filter(Mapping::isAuthorization)
                    .filter(m -> !this.service.existsByPath(m.getUrl()))
                    .forEach(mapping -> {
                        DefaultPermission permission = new DefaultPermission();
                        permission.setPath(mapping.getUrl());
                        permission.setMethod(mapping.getMethod());
                        permission.setKey(mapping.getUrl());
                        permission.setName(Optional.ofNullable(mapping.getDetail()).orElseGet(mapping::getUrl));
                        mappings.stream()
                                .filter(path -> !path.getUrl().equals(mapping.getUrl()))
                                .filter(path -> matcher.matcher(mapping.getMethod(), mapping.getUrl(), path))
                                .max(Comparator.comparing(Mapping::getUrl))
                                .ifPresent(parent -> permission.setParent(parent.getUrl()));
                        this.service.autoSave(permission);
                    });
        }
    }

}
