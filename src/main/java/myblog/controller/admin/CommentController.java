package myblog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import myblog.service.CommentService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.Result;
import myblog.util.ResultGenerator;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 评论模块Controller
 *
 * @author FANG
 */
@Slf4j(topic = "CommentController")
@Controller
@RequestMapping(value = "/admin")
public class CommentController {

    @Resource
    CommentService commentServiceImpl;

    @GetMapping(value = "/comments")
    public String getCommentIndex(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }

    @GetMapping(value = "/comments/list")
    @ResponseBody
    public Result getCommentList(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = commentServiceImpl.getCommentList(pageQueryUtils);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping(value = "/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentServiceImpl.checkDown(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @PostMapping(value = "/comments/delete")
    @ResponseBody
    public Result deleteComments(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentServiceImpl.deleteCommentBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result reply(@RequestParam("commentId") Long commentId,
                            @RequestParam("replyBody") String replyBody) {
        if (commentId == null || commentId < 1 || StringUtils.isEmpty(replyBody)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentServiceImpl.reply(commentId, replyBody)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("回复失败");
        }
    }

}
