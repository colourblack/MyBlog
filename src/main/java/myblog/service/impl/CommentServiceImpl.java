package myblog.service.impl;

import lombok.extern.slf4j.Slf4j;
import myblog.entity.BlogComment;
import myblog.mappers.BlogCommentMapper;
import myblog.service.CommentService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FANG
 */
@Service
@Slf4j(topic = "CommentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    BlogCommentMapper blogCommentMapper;

    @Override
    public PageResult getCommentList(PageQueryUtils pageQueryUtils) {
        int total = blogCommentMapper.getTotalCount(pageQueryUtils);
        List<BlogComment> blogComments = blogCommentMapper.getCommentList(pageQueryUtils);
        return new PageResult(total, pageQueryUtils.getLimit(), pageQueryUtils.getPage(), blogComments);
    }

    @Override
    public boolean checkDown(Integer[] ids) {
        return blogCommentMapper.checkDown(ids) > 0;
    }

    @Override
    public boolean deleteCommentBatch(Integer[] ids) {
        return blogCommentMapper.deleteBatch(ids) > 0;
    }

    @Override
    public boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(commentId);
        //blogComment不为空且状态为已审核，则继续后续操作
        if (blogComment != null && blogComment.getCommentStatus().intValue() == 1) {
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return blogCommentMapper.updateByPrimaryKeySelective(blogComment) > 0;
        }
        return false;
    }

    @Override
    public boolean addComment(BlogComment comment) {
        return blogCommentMapper.insertSelective(comment) > 0;
    }

    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, Integer commentPage) {
        if (commentPage < 1) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", commentPage);
        params.put("blogId", blogId);
        params.put("limit", 9);
        params.put("commentStatus", 1);
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        List<BlogComment> blogCommentList = blogCommentMapper.getCommentList(pageQueryUtils);
        if (!CollectionUtils.isEmpty(blogCommentList)) {
            int total = blogCommentMapper.getTotalCount(pageQueryUtils);
            return new PageResult(total,pageQueryUtils.getLimit(), pageQueryUtils.getPage(),blogCommentList);
        }
        return null;
    }
}
