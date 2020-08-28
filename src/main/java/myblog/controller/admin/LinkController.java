package myblog.controller.admin;

import myblog.entity.BlogLink;
import myblog.entity.BlogTagRelation;
import myblog.service.LinkService;
import myblog.util.PageQueryUtils;
import myblog.util.PageResult;
import myblog.util.Result;
import myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 友情链接 Controller
 * @author FANG
 */
@Controller
@RequestMapping(value = "/admin")
public class LinkController {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";

    @Resource
    LinkService linkServiceImpl;

    @RequestMapping(value = "/links", method = RequestMethod.GET)
    public String getLinks(HttpServletRequest request){
        request.setAttribute("path","links");
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public Result getLinkList(@RequestParam Map<String, Object>params){
        if (StringUtils.isEmpty(params.get(PAGE)) || StringUtils.isEmpty(params.get(LIMIT))) {
            return ResultGenerator.genFailResult("参数错误");
        }
        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult linkList = linkServiceImpl.getLinkList(pageQueryUtils);
        return ResultGenerator.genSuccessResult(linkList);
    }

    @PostMapping("/links/save")
    @ResponseBody
    @Transactional
    public Result saveLink(@RequestParam("linkType") Integer linkType,
                           @RequestParam("linkName")String linkName,
                           @RequestParam("linkUrl")String linkUrl,
                           @RequestParam("linkDescription")String linkDescription,
                           @RequestParam("linkRank")Integer linkRank){
        if (linkType == null || linkType < 0  || linkRank < 1
                || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        BlogLink blogLink = new BlogLink();
        blogLink.setLinkDescription(linkDescription);
        blogLink.setLinkName(linkName);
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkRank(linkRank);
        blogLink.setLinkUrl(linkUrl);
        if (linkServiceImpl.saveLink(blogLink)) {
            return ResultGenerator.genSuccessResult(blogLink);
        }
        return ResultGenerator.genFailResult("添加失败！");
    }

    @PostMapping("/links/update")
    @ResponseBody
    @Transactional
    public Result updateLink(@RequestParam("linkId") Integer linkId,
                             @RequestParam("linkType") Integer linkType,
                             @RequestParam("linkName")String linkName,
                             @RequestParam("linkUrl")String linkUrl,
                             @RequestParam("linkDescription")String linkDescription,
                             @RequestParam("linkRank")Integer linkRank) {
        if (linkId == null || linkId < 1 || linkType == null || linkType < 0 || linkRank < 1
                || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        BlogLink blogLink = new BlogLink();
        blogLink.setLinkId(linkId);
        blogLink.setLinkDescription(linkDescription);
        blogLink.setLinkName(linkName);
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkRank(linkRank);
        blogLink.setLinkUrl(linkUrl);
        if (linkServiceImpl.updateLink(blogLink)) {
            return ResultGenerator.genSuccessResult(blogLink);
        }
        return ResultGenerator.genFailResult("更新链接失败！");
    }

    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result getInfo(@PathVariable("id") Integer linkId){
        if (linkId < 1) {
            return ResultGenerator.genFailResult("参数错误！");
        }
        BlogLink blogLink = linkServiceImpl.selectLinkById(linkId);
        if (blogLink == null) {
            return ResultGenerator.genFailResult("不存在该Id");
        } else {
            return ResultGenerator.genSuccessResult(blogLink);
        }
    }

    @PostMapping("/links/delete")
    @ResponseBody
    public Result deleteLink(@RequestBody Integer[] ids){
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数错误");
        }
        if (linkServiceImpl.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("批量删除失败！");
        }
    }
}
