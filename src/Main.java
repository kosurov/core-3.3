import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static void main(String[] args) {
        String zipFilePath = "D:\\Games\\GameOfTheYear\\savegames\\saves.zip";
        String dirPath = "D:\\Games\\GameOfTheYear\\savegames";
        openZip(zipFilePath, dirPath);

        File saveGamesDir = new File(dirPath);
        List<File> savesList = null;

        if (saveGamesDir.isDirectory()) {
            savesList = Arrays.stream(saveGamesDir.listFiles())
                    .filter(s -> s.getName().contains("dat"))
                    .collect(Collectors.toList());
        }

        for (File save : savesList) {
            System.out.println(openProgress(save.getPath()));
        }
    }

    public static void openZip(String zipFilePath, String dirPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            String name;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(dirPath + "\\" + name);
                for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    fileOutputStream.write(c);
                }
                fileOutputStream.flush();
                zipInputStream.closeEntry();
                fileOutputStream.close();
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static GameProgress openProgress(String filePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            gameProgress = (GameProgress) objectInputStream.readObject();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return gameProgress;
    }
}
