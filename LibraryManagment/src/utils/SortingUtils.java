package utils;

import model.BookBase;

import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    // Lambda: сортировка по цене (по возрастанию)
    public static void sortByPriceAsc(List<BookBase> books) {
        books.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
    }

    // Method reference (тоже ок как lambda-использование): сортировка по названию
    public static void sortByTitleAsc(List<BookBase> books) {
        books.sort(Comparator.comparing(BookBase::getTitle, String.CASE_INSENSITIVE_ORDER));
    }

    // Lambda: сортировка по году (по убыванию)
    public static void sortByYearDesc(List<BookBase> books) {
        books.sort((a, b) -> Integer.compare(b.getPublishYear(), a.getPublishYear()));
    }
}
