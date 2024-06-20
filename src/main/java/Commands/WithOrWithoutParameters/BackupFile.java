package Commands.WithOrWithoutParameters;

import Commands.Commands;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.IncorrectValueException;
import OptionsExceptions.UnknownArgsException;
import Tools.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class BackupFile implements Commands {

    // Папка с бэкапами
    private static final String path_to_backup_folder = "C:\\Users\\ivanb\\Documents\\Backups\\";
    // Файлы для бэкапа
    private static final Path[] files_need_backup = {
            Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Txt\\Access.txt"),
            Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Txt\\TestingAccess.txt"),
            Paths.get("C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\KeysStorage.ks")
    };

    @Override
    public String perform(String postfix) throws IOException, UnknownArgsException, AccessNotFoundException, IncorrectValueException {

        if(postfix.isEmpty())
            throw new UnknownArgsException("Нужно указать параметр");

        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if (args.length > 1)
            throw new UnknownArgsException("Параметров больше чем нужно");
        if (!args[0].equals("make"))
            throw new UnknownArgsException("Неверный параметр");

        CheckFiles checkFiles = new CheckFiles();
        if(checkFiles.inspect(true))
            return manualCreate();
        else
            return "Файлы не прошли проверку";
    }

    public String manualCreate() throws IOException {

        Path folder = Paths.get(path_to_backup_folder);
        List<File> files = getFilesFromFolder(folder);

        StringJoiner joiner = new StringJoiner("\n");
        for (Path path : files_need_backup) {
            String name = path.getFileName().toString();

            Optional<File> optionalFile = files.stream()
                    .filter(file -> file.getName().contains("copy_".concat(name)))
                    .findFirst();

            if(optionalFile.isPresent()) {
                File file = optionalFile.get();
                Files.delete(file.toPath());
            }
            createBackup(path);
            joiner.add("Создан новый бэкап для файла - " + name);
        }

        return joiner.toString();
    }

    public String autoCreate() throws IOException {

        CheckFiles checkFiles = new CheckFiles();
        if(!checkFiles.inspect(false))
            return "Файлы не прошли проверку";

        Path folder = Paths.get(path_to_backup_folder);
        List<File> files = getFilesFromFolder(folder);

        StringJoiner joiner = new StringJoiner("\n");
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
                                createBackup(path);
                                joiner.add("Создан новый бэкап для файла - " + name);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, () -> { // Бэкап файла нету
                        try {
                            createBackup(path);
                            joiner.add("Создан новый бэкап для файла - " + name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return joiner + "\n";
    }

    private List<File> getFilesFromFolder(Path folder) throws IOException {
        try (Stream<Path> pathStream = Files.list(folder)) {
            return pathStream.map(Path::toFile).toList();
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
