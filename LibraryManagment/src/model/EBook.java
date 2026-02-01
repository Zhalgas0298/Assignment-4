package model;

public class EBook extends BookBase {

    private double fileSizeMb;
    private String downloadUrl;

    public EBook(int id, String title, double price, int publishYear, Author author,
                 double fileSizeMb, String downloadUrl) {
        super(id, title, price, publishYear, author);
        this.fileSizeMb = fileSizeMb;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String getFormat() {
        return "EBOOK";
    }

    @Override
    public int getLoanPeriodDays() {
        return 7;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setFileSizeMb(double fileSizeMb) {
        this.fileSizeMb = fileSizeMb;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public void validate() {
        super.validate();
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("File size must be > 0");
        }
        if (downloadUrl == null || downloadUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Download URL must not be empty");
        }
    }
}
