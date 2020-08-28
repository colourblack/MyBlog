package myblog.service.impl;

import myblog.entity.BlogLink;
import myblog.mappers.BlogLinkMapper;
import myblog.service.LinkService;
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
public class LinkServiceImpl implements LinkService {

    @Resource
    BlogLinkMapper blogLinkMapper;

    @Override
    @Transactional
    public PageResult getLinkList(PageQueryUtils pageQueryUtils) {
        int totalCounts = blogLinkMapper.selectTotalLinks();
        List<BlogLink> blogLinks = blogLinkMapper.getBlogLinkList(pageQueryUtils);
        return new PageResult(totalCounts, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), blogLinks);
    }

    @Override
    public boolean saveLink(BlogLink blogLink) {
        return blogLinkMapper.insertSelective(blogLink) > 0;
    }

    @Override
    public BlogLink selectLinkById(Integer linkId) {
        return blogLinkMapper.selectByPrimaryKey(linkId);
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }

    @Override
    public boolean updateLink(BlogLink blogLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(blogLink) > 0;
    }
}
