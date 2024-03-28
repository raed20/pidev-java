package interfaces;

import entities.Blog;

import java.util.List;

public interface IBlog {
    void addBlog(Blog Blog);

    void deleteBlog(int id);

    void updateBlog(Blog Blog);

    List<Blog> getAllBlog();
    List<Blog> getCommentaireByBlogId(int CommentaireId);

}