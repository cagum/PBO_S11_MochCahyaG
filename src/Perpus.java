import java.io.*;
import java.util.*;

public class Perpus {

    private static final String BOOK_FILE_NAME = "library.csv";
    private static final String BORROWER_FILE_NAME = "borrowers.csv";
    private static final String CSV_SEPARATOR = ",";

    private static final int ISBN_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int AVAILABLE_INDEX = 3;

    private static final int BORROWER_ISBN_INDEX = 0;
    private static final int BORROWER_NAME_INDEX = 1;
    private static final int BORROW_DATE_INDEX = 2;

    public static void main(String[] args) {
        // Check if files exist, if not create them with headers
        createFileIfNotExists(BOOK_FILE_NAME, "ISBN,Title,Author,Available\n123456789,Java Programming,John Doe,1\n987654321,Data Structures,Alice Smith,1\n");
        createFileIfNotExists(BORROWER_FILE_NAME, "ISBN,Borrower Name,Borrow Date\n");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("1. Pinjam buku");
                System.out.println("2. Kembalikan buku");
                System.out.println("3. Lihat daftar buku");
                System.out.println("4. Keluar");
                System.out.print("Pilih menu: ");
                int choice = 0;
                
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Input tidak valid, harap masukkan angka.");
                    scanner.next(); // Clear the invalid input
                    continue;
                }
                
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        borrowBook();
                        break;
                    case 2:
                        returnBook();
                        break;
                    case 3:
                        listBooks();
                        break;
                    case 4:
                        System.out.println("Terima kasih!");
                        System.exit(0);
                    default:
                        System.out.println("Pilihan tidak valid");
                }
            }
        }
    }

    private static void createFileIfNotExists(String fileName, String initialContent) {
        File file = new File(fileName);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                bw.write(initialContent);
                System.out.println("File " + fileName + " berhasil dibuat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void borrowBook() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan ISBN buku yang ingin dipinjam: ");
            String isbn = scanner.nextLine();
            System.out.print("Masukkan nama peminjam: ");
            String borrowerName = scanner.nextLine();

            List<String[]> bookLines = readData(BOOK_FILE_NAME);
            List<String[]> borrowerLines = readData(BORROWER_FILE_NAME);

            for (String[] line : bookLines) {
                if (line[ISBN_INDEX].equals(isbn)) {
                    if (line[AVAILABLE_INDEX].equals("1")) {
                        line[AVAILABLE_INDEX] = "0";
                        borrowerLines.add(new String[]{isbn, borrowerName, new Date().toString()});
                        System.out.println("Buku berhasil dipinjam");
                    } else {
                        System.out.println("Buku sedang dipinjam");
                    }
                    saveData(BOOK_FILE_NAME, bookLines);
                    saveData(BORROWER_FILE_NAME, borrowerLines);
                    return;
                }
            }
        }

        System.out.println("Buku tidak ditemukan");
    }

    private static void returnBook() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan ISBN buku yang ingin dikembalikan: ");
            String isbn = scanner.nextLine();

            List<String[]> bookLines = readData(BOOK_FILE_NAME);
            List<String[]> borrowerLines = readData(BORROWER_FILE_NAME);

            for (String[] line : bookLines) {
                if (line[ISBN_INDEX].equals(isbn)) {
                    line[AVAILABLE_INDEX] = "1";
                    System.out.println("Buku berhasil dikembalikan");
                    saveData(BOOK_FILE_NAME, bookLines);
                    // Remove borrower entry
                    borrowerLines.removeIf(borrowerLine -> borrowerLine[BORROWER_ISBN_INDEX].equals(isbn));
                    saveData(BORROWER_FILE_NAME, borrowerLines);
                    return;
                }
            }
        }

        System.out.println("Buku tidak ditemukan");
    }

    private static void listBooks() {
        List<String[]> bookLines = readData(BOOK_FILE_NAME);

        System.out.println("Daftar Buku:");
        System.out.println("ISBN\tTitle\tAuthor\tAvailable");
        for (String[] line : bookLines) {
            System.out.println(line[ISBN_INDEX] + "\t" + line[TITLE_INDEX] + "\t" + line[AUTHOR_INDEX] + "\t" + (line[AVAILABLE_INDEX].equals("1") ? "Yes" : "No"));
        }
    }

    private static List<String[]> readData(String fileName) {
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(CSV_SEPARATOR);
                lines.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static void saveData(String fileName, List<String[]> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            if (fileName.equals(BOOK_FILE_NAME)) {
                // Write the header for books
                bw.write("ISBN,Title,Author,Available");
            } else if (fileName.equals(BORROWER_FILE_NAME)) {
                // Write the header for borrowers
                bw.write("ISBN,Borrower Name,Borrow Date");
            }
            bw.newLine();
            for (String[] line : lines) {
                bw.write(String.join(CSV_SEPARATOR, line));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
