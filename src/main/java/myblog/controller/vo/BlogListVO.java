package myblog.controller.vo;

import java.io.Serializable;

/**
 * 首页的view object封装
 * @author FANG
 */
public class BlogListVO implements Serializable {

    private Long blogId;

    private String blogTitle;

    private String blogCoverImage;

    private Integer blogCategoryId;

    private String blogCategoryIcon;

    private String blogCategoryName;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogCoverImage() {
        return blogCoverImage;
    }

    public void setBlogCoverImage(String blogCoverImage) {
        this.blogCoverImage = blogCoverImage;
    }

    public Integer getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(Integer blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getBlogCategoryIcon() {
        return blogCategoryIcon;
    }

    public void setBlogCategoryIcon(String blogCategoryIcon) {
        this.blogCategoryIcon = blogCategoryIcon;
    }

    public String getBlogCategoryName() {
        return blogCategoryName;
    }

    public void setBlogCategoryName(String blogCategoryName) {
        this.blogCategoryName = blogCategoryName;
    }

    @Override
    public String toString() {
        return "BlogListVO{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogCoverImage='" + blogCoverImage + '\'' +
                ", blogCategoryId=" + blogCategoryId +
                ", blogCategoryIcon='" + blogCategoryIcon + '\'' +
                ", blogCategoryName='" + blogCategoryName + '\'' +
                '}';
    }
}
