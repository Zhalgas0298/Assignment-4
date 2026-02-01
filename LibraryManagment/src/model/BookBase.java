package model;

public abstract class BookBase implements Validatable, PricedItem {
    private int id;
    private String Title;
    private double Price;
    private int PublishYear;

    private Author author;

    public BookBase(int id, String Title, double Price, int PublishYear, Author author) {
        this.id = id;
        this.Title = Title;
        this.Price = Price;
        this.PublishYear = PublishYear;
        this.author = author;
    }

    public abstract String getFormat();
    public abstract int getLoanPeriodDays();

    public String printInfo () {
        String authorName = (author == null) ? "Unknown" : author.getFullName();
        return String.format(
                "Book[id=%d, Title='%s', Author='%s', Year=%d, Format=%s, price=%,2f]",
                id, Title, authorName, PublishYear, getFormat(), Price
        );
    }



    public int getId(){
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public int getPublishYear() {
        return PublishYear;
    }

    public Author getAuthor() {
        return author;
    }

    public void setTitle(String title){
        this.Title = Title;
    }

    public void getPublishYear(int publishYear) {
        this.PublishYear = PublishYear;
    }

    public void setAuthor(Author author){
        this.author = author;
    }

    @Override
    public double getPrice() {
        return Price;
    }

    @Override
    public void setPrice(double Price) {
        this.Price = Price;
    }

    @Override
    public void validate() {
        if (Title == null || Title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (Price <= 0) {
            throw new IllegalArgumentException("Price must be less 0");
        }
        if (PublishYear < 1200 || PublishYear > 2050) {
            throw new IllegalArgumentException("Publish year must be between 1200 and 2050");
        }
        if (author == null) {
            throw new IllegalArgumentException("Author must not be null");
        }
    }
}