package myblog.service.impl;

import lombok.extern.slf4j.Slf4j;
import myblog.entity.BlogCategory;
import myblog.mappers.BlogCategoryMapper;
import myblog.service.CategoryService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author FANG
 */
@Service
@Slf4j(topic = "CategoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    BlogCategoryMapper blogCategoryMapper;

    @Override
    public PageResult getBlogCategoryPage(PageQueryUtils pageQueryUtils) {
        List<BlogCategory> blogCategoryList = blogCategoryMapper.findCategoryList(pageQueryUtils);
        int totalCount = blogCategoryMapper.getTotalCategories();
        log.debug("Method: getBlogCategoryPage --> totalCount = {}", totalCount);
        return new PageResult(totalCount, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), blogCategoryList);
    }

    @Override
    @Transactional
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        BlogCategory blogCategory = blogCategoryMapper.selectByCategoryName(categoryName);
        if (blogCategory == null) {
            blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        log.debug("Method: saveCategory --> blogCategory already exists");
        blogCategory = null;
        return false;
    }

    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null) {
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    public BlogCategory selectCategoryById(Integer categoryId) {
        return blogCategoryMapper.selectByPrimaryKey(categoryId);
    }

    @Override
    public Integer delete(Integer[] ids) {
        return blogCategoryMapper.deleteBatch(ids);
    }

    @Override
    public List<BlogCategory> getAllCategory() {
        return blogCategoryMapper.findCategoryList(null);
    }
}
