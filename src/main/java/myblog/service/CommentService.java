package myblog.service;

import myblog.entity.BlogComment;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;

/**
 * @author FANG
 */
public interface CommentService {

    /**
     * 获取评论分页列表
     * @param pageQueryUtils 分页参数
     * @return 分页结果
     */
    PageResult getCommentList(PageQueryUtils pageQueryUtils);

    /**
     * 审核评论
     * @param ids 评论的id集合
     * @return 审核结果
     */
    boolean checkDown(Integer[] ids);

    /**
     * 删除评论
     * @param ids ids 评论的id集合
     * @return 删除结果
     */
    boolean deleteCommentBatch(Integer[] ids);


    boolean reply(Long commentId, String replyBody);

    boolean addComment(BlogComment comment);

    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, Integer commentPage);

}
