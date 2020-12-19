package tech.guyi.web.quick.permission.admin.defaults.encryption.defaults;

import org.springframework.util.DigestUtils;
import tech.guyi.web.quick.permission.admin.defaults.encryption.AdminPasswordEncryption;

public class DefaultAdminPasswordEncryption implements AdminPasswordEncryption {

    @Override
    public String encryption(String origin) {
        return DigestUtils.md5DigestAsHex(origin.getBytes());
    }

}
