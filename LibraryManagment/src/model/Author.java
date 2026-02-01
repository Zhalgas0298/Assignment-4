package model;

public class Author {
    private int id;
    private String FullName;
    private String Country;

    public Author(int id, String FullName, String Country) {
        this.id = id;
        this.FullName = FullName;
        this.Country = Country;
    }

    public int getId(){
        return id;
    }

    public String getFullName(){
        return FullName;
    }

    public String getCountry(){
        return Country;
    }

    public void setFullName(String FullName){
        this.FullName = FullName;
    }

    public void setCountry(String Country){
        this.Country = Country;
    }
}
