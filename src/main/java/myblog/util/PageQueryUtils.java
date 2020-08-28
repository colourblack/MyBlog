package myblog.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 封装前台传入的分页请求参数
 *
 * @author FANG
 */
public class PageQueryUtils extends LinkedHashMap<String, Object> {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer limit;

    public PageQueryUtils(Map<String, Object> params) {
        this.putAll(params);

        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", this.page);
        this.put("limit", this.limit);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageQueryUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
