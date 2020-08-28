package myblog.service;

import myblog.entity.BlogLink;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.Result;

/**
 * @author FANG
 */
public interface LinkService {


    /**
     * 获取分页结果
     * @param pageQueryUtils 分页参数
     * @return 分页结果
     */
    PageResult getLinkList(PageQueryUtils pageQueryUtils);

    /**
     * 存储链接Link
     * @param blogLink 链接对象Link
     * @return 是否成功
     */
    boolean saveLink(BlogLink blogLink);

    BlogLink selectLinkById(Integer linkId);

    boolean deleteBatch(Integer[] ids);

    boolean updateLink(BlogLink blogLink);
}
