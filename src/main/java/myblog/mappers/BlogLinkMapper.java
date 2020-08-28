package myblog.mappers;

import myblog.entity.BlogLink;
import myblog.util.PageQueryUtils;

import java.util.List;

public interface BlogLinkMapper {
    int deleteByPrimaryKey(Integer linkId);

    int insert(BlogLink record);

    int insertSelective(BlogLink record);

    BlogLink selectByPrimaryKey(Integer linkId);

    List<BlogLink> getBlogLinkList(PageQueryUtils params);

    int deleteBatch(Integer[] ids);

    int selectTotalLinks();

    int updateByPrimaryKeySelective(BlogLink record);

    int updateByPrimaryKey(BlogLink record);
}