package tech.guyi.web.quick.permission.admin.defaults.db;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Transactional(transactionManager = "transactionManagerAdmin")
public @interface DefaultAdminTransactional {
}
