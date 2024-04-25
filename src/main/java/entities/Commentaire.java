package entities;
import java.util.Collection;
public class Commentaire {
    private int id;
    private String Content;
    private int blog_id;

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

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


}
