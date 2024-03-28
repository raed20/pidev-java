package entities;


public class Blog {
    private int id;
    private String Title;
    private String Description;
    private String Content;
    private int Comment;

    public Blog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getComment() {
        return Comment;
    }

    public void setComment(Integer Comment) {
        this.Comment = Comment;
    }


}