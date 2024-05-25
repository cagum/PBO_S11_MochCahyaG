import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Perpus {
    private static final String FILE_NAME = "library.csv";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Terima input dari pengguna
            System.out.print("Masukkan nama: ");
            String name = scanner.nextLine();

            System.out.print("Masukkan kelas: ");
            String className = scanner.nextLine();

            System.out.print("Masukkan kode buku: ");
            String bookCode = scanner.nextLine();

            // Simpan data ke file CSV
            try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
                writer.append(name)
                      .append(',')
                      .append(className)
                      .append(',')
                      .append(bookCode)
                      .append('\n');
                System.out.println("Data berhasil ditambahkan!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
