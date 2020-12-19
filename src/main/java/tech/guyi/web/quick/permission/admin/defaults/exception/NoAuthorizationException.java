package tech.guyi.web.quick.permission.admin.defaults.exception;

public class NoAuthorizationException extends RuntimeException {

    public NoAuthorizationException(){
        super("请先登录");
    }

}
