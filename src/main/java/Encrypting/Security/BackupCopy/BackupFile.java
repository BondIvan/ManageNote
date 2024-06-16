package Encrypting.Security.BackupCopy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BackupFile {

    // Папка с бэкапами
    private static final String path_to_backup_folder = "C:\\Users\\ivanb\\Documents\\Backups\\";
    // Файлы для бэкапа
    private static final Path[] files_need_backup = {
        Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Txt\\Access.txt"),
        Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Txt\\TestingAccess.txt"),
        Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\KeysStorage.ks")
    };

    public void create() throws IOException {

        Path folder = Paths.get(path_to_backup_folder);
        List<File> files = Files.list(folder)
                .map(Path::toFile)
                .toList();

        for (Path path : files_need_backup) {
            String name = path.getFileName().toString();
            files.stream()
                    .filter(file -> file.getName().contains("copy_".concat(name)))
                    .findFirst()
                    .ifPresentOrElse(file -> {
                        String fileName = file.getName();
                        if (checkTimeInterval(fileName)) {
                            try {
                                Files.delete(file.toPath());
                                System.out.println("Создание нового бэкапа для файла - " + name);
                                createBackup(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, () -> {
                        try {
                            System.out.println("Создание нового бэкапа для файла " + name);
                            createBackup(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }

    }

    // Создать бэкап
    private void createBackup(Path sourcePath) throws IOException {

        String fileName = sourcePath.getFileName().toString();
        Path target = Paths.get(createBackupFileName(fileName));

        Files.copy(sourcePath, target, StandardCopyOption.REPLACE_EXISTING);
    }
    // Сформироватаь имя для бэкап файла
    private String createBackupFileName(String fileName) {

        String currentDate = getCurrentDate();
        String full_name = path_to_backup_folder.concat(currentDate).concat(" copy_").concat(fileName);

        return full_name;
    }
    // Получить текущую дату в виде строки
    private String getCurrentDate() {

        DateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd]");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String currentDate = dateFormat.format(date);

        return currentDate;
    }
    // Проверка, прошло ли 2 дня после создания полседнего бэкапа
    private boolean checkTimeInterval(String backupName) {

        String forParse = backupName.substring(0, 12);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd]");
        try {
            Date date = simpleDateFormat.parse(forParse);

            long twoDays = date.getTime() + 172800000L; // + 2 дня в миллисикундах
            Date afterTwoDays = new Date(twoDays);

            Calendar backupCalendar = new GregorianCalendar();
            backupCalendar.setTimeInMillis(afterTwoDays.getTime());

            Calendar currentCalendar = Calendar.getInstance();

            return currentCalendar.after(backupCalendar);
        } catch (ParseException e) {
            System.err.println("Ошибка в парсинге даты: " + e.getMessage());
        }

        return false;
    }

}
