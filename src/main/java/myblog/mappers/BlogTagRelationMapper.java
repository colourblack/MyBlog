package myblog.mappers;

import myblog.entity.BlogTagRelation;

import java.util.List;

public interface BlogTagRelationMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(BlogTagRelation record);

    int insertSelective(BlogTagRelation record);

    int insertBatch(List<BlogTagRelation> blogTagRelations);

    int deleteByBlogId(Long blogId);

    BlogTagRelation selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(BlogTagRelation record);

    int updateByPrimaryKey(BlogTagRelation record);
}