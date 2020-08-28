package myblog.mappers;

import myblog.entity.BlogTag;
import myblog.entity.BlogTagCount;

import java.util.List;
import java.util.Map;

/**
 * @author FANG
 */
public interface BlogTagMapper {
    int deleteByPrimaryKey(Integer tagId);

    int deleteBatch(Integer[] ids);

    int insert(BlogTag record);

    int insertSelective(BlogTag record);

    int insertByNameBatchSelective(List<BlogTag> blogTags);

    BlogTag selectByPrimaryKey(Integer tagId);

    BlogTag selectByTagName(String tagName);

    List<BlogTag> findTagList(Map<String, Object> params);

    List<BlogTagCount> findBlogTagCount();

    int getTagCounts();

    int updateByPrimaryKeySelective(BlogTag record);

    int updateByPrimaryKey(BlogTag record);
}