package myblog.service;

import myblog.controller.vo.BlogDetailVO;
import myblog.controller.vo.SimpleyBlogListVO;
import myblog.entity.Blog;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;

import java.util.List;

public interface BlogService {

    /**
     * 上传博客
     * @param blog 前台上传的博客对象
     * @return 是否上传成功
     */
    String saveBlog(Blog blog);

    /**
     * 获取博客列表
     * @param params 传入的map参数
     * @return 返回通用Result
     */
    PageResult getBlogList(PageQueryUtils params);

    /**
     * 通过博客删除id
     * @param ids Array of blogId
     * @return 返回通用result
     */
    int deleteBlogs(Integer[] ids);

    /**
     * 更新博客
     * @param blog 需要更新的博客对象
     * @return 是否更新成功
     */
    String updateBlog(Blog blog);

    /**
     * 通过文章id获取文章
     * @param id blogId
     * @return blog
     */
    Blog getBlogById(Long id);

    /**
     * 返回首页分页结果
     * @param pageNum 当前页面
     * @return 首页分页结果
     */
    PageResult blogListForIndex(Integer pageNum);

    /**
     * 根据类型返回首页文章列表
     * @param type type = 1 最新博客
     *             type = 0 最多阅读博客
     * @return 符合条件的首页文章列表
     */
    List<SimpleyBlogListVO> getBlogListForIndexPage(int type);

    /**
     * 根据博客id返回对应的博客详情
     * @param blogId 博客id
     * @return 博客的详情
     */
    BlogDetailVO getBlogDetailByBlogId(Long blogId);

    /**
     * 根据分类名称获取对应类别的博客列表
     * @param blogCategoryName 博客分类名称
     * @param pageNum 当前页数
     * @return 分页结果
     */
    PageResult blogPageByCategoryName(String blogCategoryName, Integer pageNum);

    /**
     * 根据标签名以及当前页面获取分页结果
     * @param tagName 标签名称
     * @param pageNum 当前页面
     * @return 分页结果
     */
    PageResult blogPageByTagName(String tagName, Integer pageNum);

    /**
     * 根据搜索关键字获取分页信息
     * @param keyword 搜索关键字
     * @param pageNum 当前页面
     * @return 分页结果
     */
    PageResult blogPageBySearch(String keyword, Integer pageNum);

}
