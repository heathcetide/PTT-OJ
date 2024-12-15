package com.cetide.oj.model.dto.postfavour;

import com.cetide.oj.common.PageRequest;
import com.cetide.oj.model.dto.post.PostQueryRequest;

import java.io.Serializable;

/**
 * 帖子收藏查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class PostFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private PostQueryRequest postQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    // Constructor
    public PostFavourQueryRequest() {
    }

    // Getter and Setter
    public PostQueryRequest getPostQueryRequest() {
        return postQueryRequest;
    }

    public void setPostQueryRequest(PostQueryRequest postQueryRequest) {
        this.postQueryRequest = postQueryRequest;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Override equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostFavourQueryRequest)) return false;
        if (!super.equals(o)) return false;

        PostFavourQueryRequest that = (PostFavourQueryRequest) o;

        if (postQueryRequest != null ? !postQueryRequest.equals(that.postQueryRequest) : that.postQueryRequest != null) {
            return false;
        }
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (postQueryRequest != null ? postQueryRequest.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    private static final long serialVersionUID = 1L;
}
