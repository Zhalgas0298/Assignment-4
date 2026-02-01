package model;

public class PrintedBook extends BookBase {

    private int pages;
    private String shelfCode;

    public PrintedBook(int id, String title, double price, int publishYear, Author author,
                       int pages, String shelfCode) {
        super(id, title, price, publishYear, author);
        this.pages = pages;
        this.shelfCode = shelfCode;
    }

    @Override
    public String getFormat() {
        return "PRINTED";
    }

    @Override
    public int getLoanPeriodDays() {
        return 14;
    }

    public int getPages() {
        return pages;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    @Override
    public void validate() {
        super.validate();
        if (pages <= 0) {
            throw new IllegalArgumentException("Pages must be > 0");
        }
        if (shelfCode == null || shelfCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf code must not be empty");
        }
    }
}
