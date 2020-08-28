package myblog.mappers;

import myblog.entity.BlogComment;
import myblog.util.PageQueryUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogCommentMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    List<BlogComment> getCommentList(PageQueryUtils pageQueryUtils);

    int getTotalCount(PageQueryUtils pageQueryUtils);

    int checkDown(Integer[] ids);

    int deleteBatch(Integer[] ids);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);
}