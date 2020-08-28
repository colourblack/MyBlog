package myblog.controller.admin;

import myblog.service.TagService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.Result;
import myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author FANG
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    TagService tagService;

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String getTags(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @RequestMapping(value = "/tags/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getTagPage(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数错误！");
        }
        // 封装前台参数
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        // 获取分页结果
        PageResult tagPages = tagService.getTagPages(pageQueryUtils);
        // 通过ResultGenerator返回规范的Result对象
        return ResultGenerator.genSuccessResult(tagPages);
    }

    @RequestMapping(value = "/tags/save", method = RequestMethod.POST)
    @ResponseBody
    public Result saveTag(@RequestParam("tagName")String tagName){
        if (StringUtils.isEmpty(tagName)) {
            return ResultGenerator.genFailResult("参数错误!");
        }
        if (tagService.insertTag(tagName)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("添加标签失败！");
    }

    @RequestMapping(value = "/tags/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("无效参数！");
        }
        if (tagService.deleteTags(ids)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("删除标签失败！");
    }
}
