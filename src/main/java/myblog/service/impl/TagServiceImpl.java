package myblog.service.impl;


import myblog.entity.BlogTag;
import myblog.entity.BlogTagCount;
import myblog.mappers.BlogTagMapper;
import myblog.service.TagService;
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
public class TagServiceImpl implements TagService {

    @Resource
    BlogTagMapper blogTagMapper;

    @Override
    public PageResult getTagPages(PageQueryUtils params) {
        int tagCounts = blogTagMapper.getTagCounts();
        List<BlogTag> tagList = blogTagMapper.findTagList(params);
        return new PageResult(tagCounts, params.getLimit(), params.getPage(), tagList);
    }

    @Override
    @Transactional
    public boolean insertTag(String tagName) {
        BlogTag blogTag = blogTagMapper.selectByTagName(tagName);
        if (blogTag == null) {
            blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertSelective(blogTag) > 0;
        }
        blogTag = null;
        return false;
    }

    @Override
    @Transactional
    public boolean deleteTags(Integer[] ids) {
        return blogTagMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<BlogTagCount> getBlogTagCount() {
        return blogTagMapper.findBlogTagCount();
    }
}
