package myblog.service;

import myblog.entity.AdminUser;

/**
 * @author FANG
 */
public interface AdminUserService {

    /**
     * 登录
     * @param loginUserName 登录用户名
     * @param loginPassWord 登录用户密码
     * @return 返回用户信息 若不存在 则返回null
     */
    AdminUser login(String loginUserName, String loginPassWord);

    /**
     * 通过用户id查找并且返回用户信息
     * @param adminUserId 登录用户Id
     * @return 返回用户信息 若不存在 则返回null
     */
    AdminUser getAdminUserByPrimaryKey(Integer adminUserId);

    /**
     * 修用用户密码
     * @param loginUserId 当前用户id
     * @param originalPassword 当前用户密码
     * @param newPassword 当前用户期望的新密码
     * @return true 修改成功
     *         false 修改失败
     */
    Boolean updateAdminUserPassword(Integer loginUserId, String originalPassword, String newPassword);

    /**
     * 修改用户名和昵称
     * @param loginUserId 当前用户id
     * @param loginUserName 当前用户名
     * @param nickName 当前用户昵称
     * @return true 修改成功
     *         false 修改失败
     */
    Boolean updateAdminUserName(Integer loginUserId, String loginUserName, String nickName);

}
