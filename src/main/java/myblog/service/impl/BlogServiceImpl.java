package myblog.service.impl;

import lombok.extern.slf4j.Slf4j;
import myblog.controller.vo.BlogDetailVO;
import myblog.controller.vo.BlogListVO;
import myblog.controller.vo.SimpleyBlogListVO;
import myblog.entity.Blog;
import myblog.entity.BlogCategory;
import myblog.entity.BlogTag;
import myblog.entity.BlogTagRelation;
import myblog.mappers.BlogCategoryMapper;
import myblog.mappers.BlogMapper;
import myblog.mappers.BlogTagMapper;
import myblog.mappers.BlogTagRelationMapper;
import myblog.service.BlogService;
import myblog.util.MarkDownUtil;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.PatternUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FANG
 */
@Service
@Slf4j(topic = "BlogService")
public class BlogServiceImpl implements BlogService {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String DEFAULT_CATEGORY = "默认分类";

    private static final int MAX_TAGS = 6;

    @Resource
    BlogCategoryMapper blogCategoryMapperImpl;

    @Resource
    BlogTagMapper blogTagMapperImpl;

    @Resource
    BlogTagRelationMapper blogTagRelationMapperImpl;

    @Resource
    BlogMapper blogMapperImpl;

    @Override
    public Blog getBlogById(Long id) {
        return blogMapperImpl.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        // 对博客文章的类别进行处理
        BlogCategory blogCategory = blogCategoryMapperImpl.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            //blogCategory = new BlogCategory();
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        } else {
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
            // 向数据库更新category
            blogCategoryMapperImpl.updateByPrimaryKeySelective(blogCategory);
        }

        // 对博客文章标签进行处理
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > MAX_TAGS) {
            return "文章标签最多为6个";
        }

        // 将blog对象存入数据库
        if (blogMapperImpl.insertSelective(blog) > 0) {
            // 获取的所有标签
            List<BlogTag> allTags = new ArrayList<>();
            // 数据库中不存在，需要向数据库添加的新标签
            List<BlogTag> addTagsList = new ArrayList<>();
            for (String s : tags) {
                BlogTag tag = blogTagMapperImpl.selectByTagName(s);
                if (tag == null) {
                    tag = new BlogTag();
                    tag.setTagName(s);
                    addTagsList.add(tag);
                    allTags.add(tag);
                } else {
                    allTags.add(tag);
                }
            }
            // 向数据库添加新增加的标签
            if (!CollectionUtils.isEmpty(addTagsList)) {
                blogTagMapperImpl.insertByNameBatchSelective(addTagsList);
            }


            // 对博客标签关系进行处理
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            // TODO 需要处理blog_tag_relation中 blog_id 和 tag_id
            for (BlogTag tag : allTags) {
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (blogTagRelationMapperImpl.insertBatch(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "保存失败";
    }


    @Override
    public PageResult getBlogList(PageQueryUtils params) {
        int totalBlogs = blogMapperImpl.getTotalBlog(params);
        List<Blog> blogList = blogMapperImpl.findBlogList(params);
        return new PageResult(totalBlogs, params.getLimit(), params.getPage(), blogList);
    }

    @Override
    public int deleteBlogs(Integer[] ids) {
        return blogMapperImpl.deleteBatch(ids);
    }

    @Override
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapperImpl.selectByPrimaryKey(blog.getBlogId());
        if (blogForUpdate == null) {
            log.info("文章不存在！");
            return "文章不存在！";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        BlogCategory blogCategory = blogCategoryMapperImpl.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        } else {
            //设置博客分类名称
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
            blogCategoryMapperImpl.updateByPrimaryKeySelective(blogCategory);
        }

        // 对博客文章标签进行处理
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > MAX_TAGS) {
            return "文章标签最多为6个";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());

        // 将blog对象存入数据库
        if (blogMapperImpl.updateByPrimaryKeySelective(blogForUpdate) > 0) {
            // 获取的所有标签
            List<BlogTag> allTags = new ArrayList<>();
            // 数据库中不存在，需要向数据库添加的新标签
            List<BlogTag> addTagsList = new ArrayList<>();
            for (String s : tags) {
                BlogTag tag = blogTagMapperImpl.selectByTagName(s);
                if (tag == null) {
                    tag = new BlogTag();
                    tag.setTagName(s);
                    addTagsList.add(tag);
                    allTags.add(tag);
                } else {
                    allTags.add(tag);
                }
            }
            // 向数据库添加新增加的标签
            if (!CollectionUtils.isEmpty(addTagsList)) {
                blogTagMapperImpl.insertByNameBatchSelective(addTagsList);
            }
            // 对博客标签关系进行处理

            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            for (BlogTag tag : allTags) {
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (blogTagRelationMapperImpl.deleteByBlogId(blog.getBlogId()) > 0
                    && blogTagRelationMapperImpl.insertBatch(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "更新失败";
    }

    @Override
    public List<SimpleyBlogListVO> getBlogListForIndexPage(int type) {
        List<SimpleyBlogListVO> listVOS = new ArrayList<>();
        List<Blog> blogList = blogMapperImpl.findBlogListByType(type, 9);
        if (!CollectionUtils.isEmpty(blogList)) {
            for (Blog blog : blogList) {
                SimpleyBlogListVO simpleyBlogListVO = new SimpleyBlogListVO();
                BeanUtils.copyProperties(blog, simpleyBlogListVO);
                listVOS.add(simpleyBlogListVO);
            }
            return listVOS;
        }
        return null;
    }

    @Override
    @Transactional
    public PageResult blogListForIndex(Integer pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", 8);
        params.put("blogStatus", 1);
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        // 通过params获取Blog，Blog是页面的主要内容，而每个blog对应的category等信息是该blog附加的内容
        List<Blog> blogList = blogMapperImpl.findBlogList(pageQueryUtils);
        // 通过getBlogListVOListByBlogList(List<Blog> blogList)方法将符合条件的blog以及对应的category信息填充进view object
        List<BlogListVO> blogListVOList = this.getBlogListVOListByBlogList(blogList);
        int blogCounts = blogMapperImpl.getTotalBlog(pageQueryUtils);
        return new PageResult(blogCounts, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), blogListVOList);
    }

    @Override
    public BlogDetailVO getBlogDetailByBlogId(Long blogId) {
        Blog blog = blogMapperImpl.selectByPrimaryKey(blogId);
        return this.getBlogDetail(blog);
    }

    @Override
    public PageResult blogPageByCategoryName(String blogCategoryName, Integer pageNum) {
        // 因为该路径没有使用拦截器拦截，因此需要使用正则表达式判断传入分类名称是否合法
        if (!PatternUtil.validKeyword(blogCategoryName)) {
            log.info("blogPageByCategoryName --> blogCategoryName:`{}` 为非法参数", blogCategoryName);
            return null;
        }
        BlogCategory blogCategory = blogCategoryMapperImpl.selectByCategoryName(blogCategoryName);
        if (DEFAULT_CATEGORY.equals(blogCategoryName) && blogCategory == null) {
            blogCategory = new BlogCategory();
            blogCategory.setCategoryId(0);
        }
        if (pageNum > 0 && blogCategory != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("page", pageNum);
            params.put("limit", 9);
            params.put("blogStatus", 1);
            params.put("blogCategoryId", blogCategory.getCategoryId());
            PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
            List<Blog> blogList = blogMapperImpl.findBlogList(pageQueryUtils);
            List<BlogListVO> BlogListViewObject = this.getBlogListVOListByBlogList(blogList);
            int totalBlog = blogMapperImpl.getTotalBlog(pageQueryUtils);
            return new PageResult(totalBlog, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), BlogListViewObject);
        }
        return null;
    }

    @Override
    public PageResult blogPageByTagName(String tagName, Integer pageNum) {
        if (PatternUtil.validKeyword(tagName)) {
            BlogTag blogTag = blogTagMapperImpl.selectByTagName(tagName);
            if (blogTag != null && pageNum > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("tagId", blogTag.getTagId());
                params.put("limit", 9);
                params.put("blogStatus", 1);
                params.put("page", pageNum);
                PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
                List<Blog> blogList = blogMapperImpl.getBlogsPageByTagId(pageQueryUtils);
                List<BlogListVO> BlogListViewObject = this.getBlogListVOListByBlogList(blogList);
                int totalBlog = blogMapperImpl.getTotalBlog(pageQueryUtils);
                return new PageResult(totalBlog, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), BlogListViewObject);
            }
        }
        return null;
    }

    @Override
    public PageResult blogPageBySearch(String keyword, Integer pageNum) {
        if (PatternUtil.validKeyword(keyword) && pageNum > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("keyword", keyword);
            params.put("limit", 9);
            params.put("blogStatus", 1);
            params.put("page", pageNum);
            PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
            List<Blog> blogList = blogMapperImpl.findBlogList(pageQueryUtils);
            List<BlogListVO> blogListVOS = getBlogListVOListByBlogList(blogList);
            int totalBlog = blogMapperImpl.getTotalBlog(pageQueryUtils);
            return new PageResult(totalBlog, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), blogListVOS);
        }
        return null;
    }

    /**
     * 填充 blog detail view object
     * @param blog Blog对象
     * @return blog detail view object
     */
    private BlogDetailVO getBlogDetail(Blog blog) {
        // 1.获取的Blog中没有 category_icon的内容
        // 2.当BlogList中的category_id = 0, 即默认分类时，需要对view object重新进行填充
        // 3.增加阅读量
        // 4.Blog中tags属性与BlogDetailVO中tags[]属性的转换
        // 判断blog是否为null 以及 blog发布状态
        if (blog != null && blog.getBlogStatus() == 1) {
            // 增加阅读量
            blog.setBlogViews(blog.getBlogViews() + 1L);
            blogMapperImpl.updateByPrimaryKeySelective(blog);
            // 填充 blog detail view object
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            // blog的属性赋予blog detail view object
            BeanUtils.copyProperties(blog, blogDetailVO);
            // 将文本转成markdown格式
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            // blog detail view object --> tags[]赋值
            String[] tags = blog.getBlogTags().split(",");
            blogDetailVO.setBlogTags(Arrays.stream(tags).collect(Collectors.toList()));
            // blog detail view object --> BlogCategoryIcon 赋值
            BlogCategory blogCategory = blogCategoryMapperImpl.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            log.debug("Blog Detail View Object --> blogCategory : {}", blogCategory.toString());
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            blogDetailVO.setBlogCategoryId(blogCategory.getCategoryId());
            blogDetailVO.setBlogCategoryName(blogCategory.getCategoryName());
            blogDetailVO.setCommentCount(0);
            return blogDetailVO;
        }
        return null;
    }

    private List<BlogListVO> getBlogListVOListByBlogList(List<Blog> blogList) {
        // 需要处理的内容有2个：
        // 1.获取的BlogList中没有 category_icon的内容
        // 2.当BlogList中的category_id = 0, 即默认分类时，需要对view object重新进行填充
        if (!CollectionUtils.isEmpty(blogList)) {
            List<BlogListVO> listResult = new ArrayList<>();
            List<Integer> CategoryIds = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            Map<Integer, String> categoryMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(CategoryIds)) {
                List<BlogCategory> blogCategoryList = blogCategoryMapperImpl.selectCategoriesByCategoryIds(CategoryIds);
                if (!CollectionUtils.isEmpty(blogCategoryList)) {
                    categoryMap = blogCategoryList.stream().collect(Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon
                            , (oldValue, newValue) -> newValue));
                }
            }
            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                // BeanUtils.copyProperties()属于浅拷贝，只是将属性的内存地址的引用给了目标类
                BeanUtils.copyProperties(blog, blogListVO);
                // 完成copy以后，blogListVO中还有category_icon没有赋值
                if (blog.getBlogCategoryId() == 0) {
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/admin/dist/img/category/1.png");
                } else {
                    blogListVO.setBlogCategoryIcon(categoryMap.get(blog.getBlogCategoryId()));
                }
                log.debug("getBlogListVOListByBlogList --> blogListVO.BlogCoverImg: {}", blogListVO.getBlogCoverImage());
                listResult.add(blogListVO);
            }
            return listResult;
        } else {
            return null;
        }
    }
}
