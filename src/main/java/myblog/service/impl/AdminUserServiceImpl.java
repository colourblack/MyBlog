package myblog.service.impl;

import myblog.entity.AdminUser;
import myblog.mappers.AdminUserMapper;
import myblog.service.AdminUserService;
import myblog.util.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * AdminUserService 实现接口
 *
 * @author FANG
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String loginUserName, String loginPassWord) {
        String passwordMd5 = MD5Util.MD5Encode(loginPassWord, "UTF-8");
        return adminUserMapper.login(loginUserName, passwordMd5);
    }

    @Override
    public AdminUser getAdminUserByPrimaryKey(Integer adminUserId) {
        return adminUserMapper.selectByPrimaryKey(adminUserId);
    }

    @Override
    public Boolean updateAdminUserPassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
                adminUser.setLoginPassword(MD5Util.MD5Encode(newPassword, "UTF-8"));
                return adminUserMapper.updateByPrimaryKey(adminUser) > 0;
            }
        }
        return false;
    }

    @Override
    public Boolean updateAdminUserName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        if (adminUser != null) {
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            return adminUserMapper.updateByPrimaryKey(adminUser) > 0;
        }
        return false;
    }
}
