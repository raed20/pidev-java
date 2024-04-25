package entities;


public class Blog {
    private int id;
    private String Title;
    private String Description;
    private String Content;
    private String Img;

    public Blog() {}

    public Blog(String title, String description, String content, String img) {
        Title = title;
        Description = description;
        Content = content;
        Img = img;
    }

    public Blog(int id, String title, String description, String content, String img) {
        this.id = id;
        Title = title;
        Description = description;
        Content = content;
        Img = img;
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

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }


}