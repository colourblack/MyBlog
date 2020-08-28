package myblog.mappers;

import myblog.entity.BlogCategory;
import myblog.util.PageQueryUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FANG
 */
public interface BlogCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(BlogCategory record);

    int insertSelective(BlogCategory record);

    List<BlogCategory> findCategoryList(PageQueryUtils pageQueryUtils);

    List<BlogCategory> selectCategoriesByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    int getTotalCategories();

    BlogCategory selectByCategoryName(String categoryName);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    int deleteBatch(Integer[] ids);

    int updateByPrimaryKeySelective(BlogCategory record);

    int updateByPrimaryKey(BlogCategory record);
}