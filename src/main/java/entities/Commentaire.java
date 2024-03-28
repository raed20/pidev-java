package entities;
import java.util.Collection;
public class Commentaire {
    private int id;
    private String Content;
    private Collection<Blog> Blog;

    public Commentaire() {
    }

    public Commentaire(String content) {
        Content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Collection<Blog> getBlog() {
        return Blog;
    }

    public void setBlog(Collection<Blog> blog) {
        Blog = blog;
    }
}
