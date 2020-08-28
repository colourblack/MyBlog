package myblog.controller;

import lombok.extern.slf4j.Slf4j;
import myblog.controller.vo.BlogDetailVO;
import myblog.entity.Blog;
import myblog.entity.BlogComment;
import myblog.entity.BlogTag;
import myblog.service.BlogService;
import myblog.service.CommentService;
import myblog.service.TagService;
import myblog.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 首页Controller
 *
 * @author FANG
 */
@Controller
@Slf4j(topic = "MyBlogController")
public class MyBlogController {

    @Resource
    BlogService blogServiceImpl;

    @Resource
    TagService tagServiceImpl;

    @Resource
    CommentService commentServiceImpl;

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    @GetMapping(value = "/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") Integer pageNum) {
        PageResult blogPageResult = blogServiceImpl.blogListForIndex(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "首页");
        request.setAttribute("newBlogs", blogServiceImpl.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogServiceImpl.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagServiceImpl.getBlogTagCount());
        return "blog/index";
    }

    @GetMapping(value = "/blog/{blogId}")
    public String getBlogDetail(@PathVariable("blogId") Long blogId, HttpServletRequest request,
                                @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        if (blogId < 1) {
            return "error/error_404";
        }
        BlogDetailVO blogDetailVO = blogServiceImpl.getBlogDetailByBlogId(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", commentServiceImpl.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        return "blog/detail";
    }

    @GetMapping(value = "/category/{blogCategoryName}")
    public String getBlogListByCategory(HttpServletRequest request, @PathVariable("blogCategoryName") String blogCategoryName) {
        return getBlogListByCategory(request, blogCategoryName, 1);
    }

    @GetMapping(value = "/category/{blogCategoryName}/{pageNum}")
    public String getBlogListByCategory(HttpServletRequest request, @PathVariable("blogCategoryName") String blogCategoryName,
                                        @PathVariable("pageNum") Integer pageNum) {
        PageResult blogPageResult = blogServiceImpl.blogPageByCategoryName(blogCategoryName, pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("pageName", "分类");
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", blogCategoryName);
        return "blog/list";
    }

    @GetMapping(value = "/tag/{tagName}")
    public String getBlogListByTag(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return getBlogListByTag(request, tagName, 1);
    }

    @GetMapping(value = "/tag/{tagName}/{pageNum}")
    public String getBlogListByTag(HttpServletRequest request, @PathVariable("tagName") String tagName,
                                   @PathVariable("pageNum") Integer pageNum) {
        PageResult blogPageResult = blogServiceImpl.blogPageByTagName(tagName, pageNum);
        request.setAttribute("pageName", "标签");
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        return "blog/list";
    }

    @GetMapping(value = "/search/{keyword}")
    public String searchByKeyword(HttpServletRequest request, @PathVariable String keyword) {
        return searchByKeyword(request, keyword, 1);
    }

    @GetMapping(value = "/search/{keyword}/{pageNum}")
    public String searchByKeyword(HttpServletRequest request,
                                  @PathVariable(value = "keyword") String keyword,
                                  @PathVariable(value = "pageNum") Integer pageNum) {
        PageResult blogPageResult = blogServiceImpl.blogPageBySearch(keyword, pageNum);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        return "blog/list";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtil.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtil.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentServiceImpl.addComment(comment));
    }
}
