package myblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import myblog.entity.BlogCategory;
import myblog.service.CategoryService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.Result;
import myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @author FANG
 */
@Controller
@RequestMapping(value = "/admin")
@Slf4j(topic = "CategoryController")
public class CategoryController {

    @Resource
    private CategoryService categoryServiceImpl;

    @GetMapping(value = "/categories")
    public String getCategories(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
    }


    @GetMapping(value = "/categories/list")
    @ResponseBody
    public Result getPageList(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = categoryServiceImpl.getBlogCategoryPage(pageQueryUtils);
        return ResultGenerator.genSuccessResult(pageResult);
    }


    @PostMapping("/categories/save")
    @ResponseBody
    public Result saveCategory(@RequestParam("categoryName") String categoryName,
                               @RequestParam("categoryIcon") String categoryIcon) {
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标");
        }
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称");
        }
        if (categoryServiceImpl.saveCategory(categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称经存在");
        }
    }

    @PostMapping(value = "/categories/update")
    @ResponseBody
    public Result updateCategory(@RequestParam("categoryId") Integer categoryId,
                                 @RequestParam("categoryName") String categoryName,
                                 @RequestParam("categoryIcon") String categoryIcon) {
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("非法参数");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标");
        }
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称");
        }
        if (categoryServiceImpl.updateCategory(categoryId, categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("该分类已经存在");
        }
    }

    @RequestMapping(value = "/categories/info/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result getInfo(@PathVariable Integer id) {
        if (id == null || id < 1) {
            return ResultGenerator.genFailResult("非法参数");
        }
        BlogCategory blogCategory = categoryServiceImpl.selectCategoryById(id);
        if (blogCategory != null) {
            return ResultGenerator.genSuccessResult(blogCategory);
        }
        return ResultGenerator.genFailResult("无效id");
    }

    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        log.debug("Method: delete --> ids[]: {}", Arrays.toString(ids)+"");
        if (categoryServiceImpl.delete(ids) > 0) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("删除失败");
    }
}
