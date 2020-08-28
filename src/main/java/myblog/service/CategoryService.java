package myblog.service;

import myblog.entity.Blog;
import myblog.entity.BlogCategory;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;

import java.util.List;

/**
 * 分类实现接口
 * @author FANG
 */
public interface CategoryService {

    /**
     * 查询分类的分页数据
     * @param pageQueryUtils 参数封装类,该类封装了前台传入的参数
     * @return 分页结果
     */
    PageResult getBlogCategoryPage(PageQueryUtils pageQueryUtils);

    /**
     * 存储类别对象
     * @param categoryName 类别名称
     * @param categoryIcon 类别图片
     * @return 是否成功存储数据
     */
    Boolean saveCategory(String categoryName, String categoryIcon);

    /**
     * 更新类别信息
     * @param categoryId 类别对象的id
     * @param categoryName 类别对象的名称
     * @param categoryIcon 类别对象的图片
     * @return 是否成功更新类别信息
     */
    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    /**
     * 通过id查找对应类别对象
     * @param categoryId 类别对象的id
     * @return
     */
    BlogCategory selectCategoryById(Integer categoryId);

    List<BlogCategory> getAllCategory();

    Integer delete(Integer[] ids);
}
