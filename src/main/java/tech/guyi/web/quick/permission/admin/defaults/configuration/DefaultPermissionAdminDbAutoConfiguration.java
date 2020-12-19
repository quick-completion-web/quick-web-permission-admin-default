package tech.guyi.web.quick.permission.admin.defaults.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import tech.guyi.web.quick.permission.admin.defaults.db.converter.EntityAttachConverter;
import tech.guyi.web.quick.permission.admin.defaults.service.*;
import tech.guyi.web.quick.permission.admin.defaults.service.defaults.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryAdmin",
        transactionManagerRef="transactionManagerAdmin",
        basePackages = {"tech.guyi.web.quick.permission.admin.defaults.db"})
public class DefaultPermissionAdminDbAutoConfiguration {

    @Resource
    private EntityManagerFactoryBuilder builder;
    @Resource
    private DataSource dataSource;
    @Resource
    private HibernateProperties hibernateProperties;
    @Resource
    private JpaProperties jpaProperties;

    @Bean(name = "entityManagerAdmin")
    public EntityManager entityManager() {
        return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(entityManagerFactoryBean().getObject()));
    }

    @Primary
    @Bean("defaultTransactionManager")
    public TransactionManager transactionManager(@Qualifier("transactionManager") TransactionManager transactionManager){
        return transactionManager;
    }

    @Bean(name = "transactionManagerAdmin")
    public PlatformTransactionManager platformTransactionManager() {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean().getObject()));
    }

    @Bean(name = "entityManagerFactoryAdmin")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        return builder
                .dataSource(dataSource)
                .properties(hibernateProperties
                        .determineHibernateProperties(jpaProperties.getProperties(),new HibernateSettings()))
                .packages("tech.guyi.web.quick.permission.admin.defaults.db")
                .persistenceUnit("default-permission-admin").build();
    }

    @Bean
    public EntityAttachConverter entityAttachConverter(){
        return new EntityAttachConverter();
    }

    @Bean
    @ConditionalOnMissingBean(AdminService.class)
    public DefaultAdminService defaultAdminService(){
        return new DefaultAdminService();
    }

    @Bean
    @ConditionalOnMissingBean(AdminPermissionService.class)
    public DefaultAdminPasswordService defaultAdminPasswordService(){
        return new DefaultAdminPasswordService();
    }

    @Bean
    @ConditionalOnMissingBean(AdminPermissionService.class)
    public DefaultAdminPermissionService defaultAdminPermissionService(){
        return new DefaultAdminPermissionService();
    }

    @Bean
    @ConditionalOnMissingBean(AdminGroupService.class)
    public DefaultAdminGroupService defaultAdminGroupService(){
        return new DefaultAdminGroupService();
    }

    @Bean
    @ConditionalOnMissingBean(GroupService.class)
    public DefaultGroupService defaultGroupService(){
        return new DefaultGroupService();
    }

    @Bean
    @ConditionalOnMissingBean(GroupPermissionService.class)
    public DefaultGroupPermissionService defaultGroupPermissionService(){
        return new DefaultGroupPermissionService();
    }

    @Bean
    @ConditionalOnMissingBean(PermissionService.class)
    public DefaultPermissionService defaultPermissionService(){
        return new DefaultPermissionService();
    }

    @Bean
    @ConditionalOnMissingBean(SuperAdminService.class)
    public DefaultSuperAdminService defaultSuperAdminService(){
        return new DefaultSuperAdminService();
    }

}
