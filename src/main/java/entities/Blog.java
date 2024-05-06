package entities;


public class Blog {
    private int id;
    private String Title;
    private String Description;
    private String Content;

    private String Img;
    private double rating;
    public Blog() {}

    public Blog(String title, String description, String content, String img) {
        Title = title;
        Description = description;
        Content = content;
        Img = img;
        this.rating= 0.1f;
    }

    public Blog(int id, String title, String description, String content, String img,double rating) {
        this.id = id;
        Title = title;
        Description = description;
        Content = content;
        Img = img;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}