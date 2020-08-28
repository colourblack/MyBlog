package myblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import myblog.entity.Blog;
import myblog.service.BlogService;
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
import java.util.Map;

/**
 * @author FANG
 */
@Controller
@RequestMapping(value = "/admin")
@Slf4j(topic = "BlogController")
public class BlogController {

    private static final int MAX_LENGTH = 150;
    private static final int MAX_CONTENT_LENGTH = 100000;

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String SUCCESS = "success";

    @Resource
    BlogService blogServiceImpl;

    @Resource
    CategoryService categoryServiceImpl;

    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public String getBlogIndex(HttpServletRequest request) {
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }


    @RequestMapping(value = "/blogs/edit", method = RequestMethod.GET)
    public String getBlogEditing(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryServiceImpl.getAllCategory());
        return "admin/edit";
    }

    @GetMapping(value = "/blogs/edit/{blogId}")
    public String editingBlog(@PathVariable("blogId")Long blogId, HttpServletRequest request){
        request.setAttribute("path","edit");
        if (blogId < 1 ) {
            request.setAttribute("blog", null);
        }
        Blog blog = blogServiceImpl.getBlogById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", categoryServiceImpl.getAllCategory());
        return "admin/edit";
    }

    @RequestMapping(value = "/blogs/list", method = RequestMethod.GET)
    @ResponseBody
    public Result blogList(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get(PAGE)) || StringUtils.isEmpty(params.get(LIMIT))) {
            ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = blogServiceImpl.getBlogList(pageQueryUtils);
        if (pageResult != null) {
            return ResultGenerator.genSuccessResult(pageResult);
        } else {
            return ResultGenerator.genFailResult("查找列表失败");
        }
    }

    @RequestMapping(value = "/blogs/save", method = RequestMethod.POST)
    @ResponseBody
    public Result saveBlog(@RequestParam("blogTitle") String blogTitle,
                           @RequestParam(value = "blogSubUrl", required = false) String blogSubUrl,
                           @RequestParam("blogCategoryId") Integer blogCategoryId,
                           @RequestParam("blogTags") String blogTags,
                           @RequestParam("blogContent") String blogContent,
                           @RequestParam("blogCoverImage") String blogCoverImage,
                           @RequestParam("blogStatus") Byte blogStatus,
                           @RequestParam("enableComment") Byte enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入标题");
        }
        if (blogTitle.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("文章标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请设置标签");
        }
        if (blogTags.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("标签过多或标签名过长");
        }
        if (blogSubUrl != null && blogSubUrl.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogContent.trim().length() > MAX_CONTENT_LENGTH) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String saveBlogResult = blogServiceImpl.saveBlog(blog);
        if (SUCCESS.equals(saveBlogResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveBlogResult);
        }
    }

    @PostMapping(value = "/blogs/delete")
    @ResponseBody
    public Result deleteBlog(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("删除失败");
        }
        if (blogServiceImpl.deleteBlogs(ids) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }


    @PostMapping(value = "/blogs/update")
    @ResponseBody
    public Result updateBlog(@RequestParam("blogId")Long blogId,
                             @RequestParam("blogTitle") String blogTitle,
                             @RequestParam(value = "blogSubUrl", required = false) String blogSubUrl,
                             @RequestParam("blogCategoryId") Integer blogCategoryId,
                             @RequestParam("blogTags") String blogTags,
                             @RequestParam("blogContent") String blogContent,
                             @RequestParam("blogCoverImage") String blogCoverImage,
                             @RequestParam("blogStatus") Byte blogStatus,
                             @RequestParam("enableComment") Byte enableComment){
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入标题");
        }
        if (blogTitle.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("文章标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请设置标签");
        }
        if (blogTags.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("标签过多或标签名过长");
        }
        if (blogSubUrl != null && blogSubUrl.trim().length() > MAX_LENGTH) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogContent.trim().length() > MAX_CONTENT_LENGTH) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String updateBlogResult = blogServiceImpl.updateBlog(blog);
        if (SUCCESS.equals(updateBlogResult)) {
            log.info("修改成功");
            return ResultGenerator.genSuccessResult("修改成功");
        } else {
            return ResultGenerator.genFailResult(updateBlogResult);
        }
    }
}
