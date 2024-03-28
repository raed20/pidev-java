package entities;

public class Utilisateurs {
    private int id;
    private String lastname;
    private int numtel;
    private String adresse;
    private String email;
    private String password;
    private String roles;


    public Utilisateurs() {
    }


    public Utilisateurs(int id, String lastname, String email, int numtel, String adresse, String roles,  String password) {
        this.id = id;
        this.lastname = lastname;
        this.numtel = numtel;
        this.adresse = adresse;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public int getNumtel() {
        return numtel;
    }

    public void setNumtel(int numtel) {
        this.numtel = numtel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Utilisateurs{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", numtel='" + numtel + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}

