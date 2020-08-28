package myblog.controller.admin;


import lombok.extern.slf4j.Slf4j;
import myblog.entity.AdminUser;
import myblog.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author FANG
 */
@Controller
@RequestMapping("/admin")
@Slf4j(topic = "AdminController")
public class AdminController {

    @Resource
    AdminUserService adminUserServiceImpl;

    @GetMapping({"/login", "/"})
    public String login() {
        return "admin/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("userName") String loginUserName,
                        @RequestParam("password") String loginPassword,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession httpSession) {
        if (StringUtils.isEmpty(verifyCode)) {
            httpSession.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(loginPassword)) {
            httpSession.setAttribute("errorMsg", "用户名/密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = httpSession.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !kaptchaCode.equals(verifyCode)) {
            httpSession.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }

        AdminUser adminUser = adminUserServiceImpl.login(loginUserName, loginPassword);
        if (adminUser == null) {
            httpSession.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        } else {
            httpSession.setAttribute("loginUser", adminUser.getNickName());
            httpSession.setAttribute("loginUserId", adminUser.getAdminUserId());
            httpSession.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin/index";
        }
    }

    @GetMapping(value = "/index")
    public String index(HttpServletRequest request) {
        request.setAttribute("path","index");
        return "admin/index";
    }


    @GetMapping(value = "/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserServiceImpl.getAdminUserByPrimaryKey(loginUserId);
        if (null == adminUser) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping(value = "/profile/password")
    @ResponseBody
    public String passwordUpdate(@RequestParam(value = "originalPassword") String originalPassword,
                                 @RequestParam(value = "newPassword") String newPassword,
                                 HttpServletRequest request) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            log.debug("passwordUpdate() --> originalPassword: {}, newPassword: {}", originalPassword, newPassword);
            return "修改失败";
        }
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        log.debug("passwordUpdate() --> loginUserId: {}", loginUserId + "");
        if (adminUserServiceImpl.updateAdminUserPassword(loginUserId, originalPassword, newPassword)) {
            request.getSession().removeAttribute("errorMsg");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("loginUserId");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping(value = "/profile/name")
    @ResponseBody
    public String nameUpdate(@RequestParam(value = "loginUserName") String loginUserName,
                             @RequestParam(value = "nickName") String nickName,
                             HttpServletRequest request) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            log.debug("nameUpdate() --> loginUserName: {}, nickName: {}", loginUserName, nickName);
            return "修改失败";
        }
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        log.debug("nameUpdate() --> loginUserId: {}", loginUserId + "");
        if (adminUserServiceImpl.updateAdminUserName(loginUserId, loginUserName, nickName)) {
            request.getSession().setAttribute("loginUser", nickName);
            return "success";
        } else {
            return "修改失败";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("loginUser");
        httpSession.removeAttribute("loginUserId");
        httpSession.removeAttribute("errorMsg");
        return "admin/login";
    }
}
