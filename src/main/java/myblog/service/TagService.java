package myblog.service;


import myblog.entity.BlogTagCount;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;

import java.util.List;

/**
 * @author FANG
 */
public interface TagService {

    PageResult getTagPages(PageQueryUtils params);

    boolean insertTag(String tagName);

    boolean deleteTags(Integer[] ids);

    List<BlogTagCount> getBlogTagCount();
}
