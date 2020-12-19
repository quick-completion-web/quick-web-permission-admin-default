package tech.guyi.web.quick.permission.admin.defaults.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.web.quick.permission.admin.defaults.controller.AdminController;
import tech.guyi.web.quick.permission.admin.defaults.controller.GroupController;
import tech.guyi.web.quick.permission.admin.defaults.controller.PermissionController;
import tech.guyi.web.quick.permission.admin.defaults.controller.advice.ExceptionAdvice;
import tech.guyi.web.quick.permission.admin.defaults.encryption.AdminPasswordEncryption;
import tech.guyi.web.quick.permission.admin.defaults.encryption.defaults.DefaultAdminPasswordEncryption;
import tech.guyi.web.quick.permission.admin.defaults.mapping.DefaultMappingInjection;
import tech.guyi.web.quick.permission.mapping.register.MappingManagerConfiguration;
import tech.guyi.web.quick.permission.mapping.register.MappingRegister;

@Configuration
public class DefaultPermissionAdminAutoConfiguration implements MappingManagerConfiguration {

    @Override
    public void configure(MappingRegister register) {
        register.allow("/admin/login");

        register.authority()
                .pathPattern("/admin/**")
                .detail("管理员模块")
                .end();
        register.authority()
                .pathPattern("/group/**")
                .detail("管理员组模块")
                .end();
        register.authority()
                .pathPattern("/permission/**")
                .detail("权限模块")
                .end();
    }

    @Bean
    @ConfigurationProperties(prefix = "tech.guyi.web.quick.permission.default.admin")
    public DefaultAdminConfiguration defaultAdminConfiguration(){
        return new DefaultAdminConfiguration();
    }

    @Bean
    public DefaultMappingInjection defaultMappingInjection(){
        return new DefaultMappingInjection();
    }

    @Bean
    @ConditionalOnMissingBean(AdminPasswordEncryption.class)
    public DefaultAdminPasswordEncryption defaultAdminPasswordEncryption(){
        return new DefaultAdminPasswordEncryption();
    }

    @Bean
    public AdminController adminController(){
        return new AdminController();
    }

    @Bean
    public PermissionController permissionController(){
        return new PermissionController();
    }

    @Bean
    public GroupController groupController(){
        return new GroupController();
    }

    @Bean
    public ExceptionAdvice exceptionAdvice(){
        return new ExceptionAdvice();
    }

}
