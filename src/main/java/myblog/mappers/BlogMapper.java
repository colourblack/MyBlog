package myblog.mappers;

import myblog.entity.Blog;
import myblog.util.PageQueryUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    int deleteByPrimaryKey(Long blogId);

    int insert(Blog record);

    int insertSelective(Blog record);

    int deleteBatch(Integer[] ids);

    Blog selectByPrimaryKey(Long blogId);

    int getTotalBlog(PageQueryUtils pageQueryUtils);

    List<Blog> getBlogsPageByTagId(PageQueryUtils pageQueryUtils);

    List<Blog> findBlogList(Map<String, Object> params);

    /**
     * 通过type的定义值返回BlogList
     * @param type type = 1 表示需要最新的Blog
     *             type = 0 表示需要最热的Blog
     * @param limit 返回的list大小
     * @return type对应的BlogList
     */
    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);
}