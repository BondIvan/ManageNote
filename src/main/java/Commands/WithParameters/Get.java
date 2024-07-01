package Commands.WithParameters;

import Commands.Commands;
import Entity.NoteEntity;
import OptionsExceptions.AccessNotFoundException;
import OptionsExceptions.UnknownArgsException;
import Source.StartConsole;
import Tools.AutoCorrection.AutoCorrectionServiceName;
import Tools.AutoCorrection.Dictionaries;
import Tools.UsefulMethods;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.KeyStoreException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class Get implements Commands {

    private final List<NoteEntity> listWithNotes;

    public Get() {
        this.listWithNotes = StartConsole.NOTES;
    }

    public Get(List<NoteEntity> notes) {
        this.listWithNotes = notes;
    }

    @Override
    public String perform(String postfix) throws UnknownArgsException, AccessNotFoundException {

        // [cb] [serviceName] / [cb] [serviceName] [serviceLogin]
        // [cb] [show] [serviceName] / [cb] [show] [serviceName] [serviceLogin]
        // [show] [serviceName] / [show] [serviceName] [serviceLogin]

        if(postfix.isEmpty())
            throw  new UnknownArgsException("Нет параметров");

        // Разбитие postfix-а на состовляющие (конкретные аргументы команды)
        String[] args = UsefulMethods.makeArgsTrue(postfix);

        if(args.length < 1)
            throw  new UnknownArgsException("Параметров меньше чем нужно");
        if(args.length > 4)
            throw new UnknownArgsException("Параметров больше чем нужно");

        String type = (args[0].equals("cb") && args[1].equals("show") || args[0].equals("show") && args[1].equals("cb")) ? "cb show" :
                args[0].equals("cb") ? "cb"
                : args[0].equals("show") ? "show"
                : null;

        if(type == null)
            throw new UnknownArgsException("Не указан параметр");

        // [cb] [show] [serviceName] [serviceLogin]
        if(args.length == 4) {
            return controlParameters(type, args[2], args[3]);
        }

        // [cb] [serviceName] [serviceLogin] / [show] [serviceName] [serviceLogin]
        if(args.length == 3) {

            // [cb] [show] [serviceName]
            if(type.equals("cb show"))
                return controlParameters(type, args[2], null);

            return controlParameters(type, args[1], args[2]);
        }

        // [cb] [serviceName] / [show] [serviceName]
        return controlParameters(type, args[1], null);
    }

    public String controlParameters(String type, String serviceName, String serviceLogin) throws AccessNotFoundException, UnknownArgsException {

        List<NoteEntity> list = getNote(serviceName, serviceLogin);

        if(list.size() > 1 && type.contains("cb") && serviceLogin == null) {
            System.out.println("Укажите в команде логин аккаунта, который необходимо показать");
            // Отсортировать все аккаунты сервиса по названию + вывести их названия и логины
            UsefulMethods.sortNoteEntityByServiceName(listWithNotes.stream()
                    .filter(note -> note.getServiceName().split(" ")[0].equalsIgnoreCase(serviceName))
                    .collect(Collectors.toList())).forEach((note) -> System.out.println(note.getServiceName() + " -> " + note.getLogin()));

            return "Теперь введите команду";
        }

        switch (type) {
            case "cb" -> clipboard(list.get(0));
            case "show" -> printNotes(list);
            case "cb show" -> {
                clipboard(list.get(0));
                printNotes(list);
            }
            default ->
                throw new UnknownArgsException("Не верный тип параметра");
        }

        return "";
    }

    public List<NoteEntity> getNote(String serviceName, String serviceLogin) throws AccessNotFoundException {

        List<NoteEntity> accounts = UsefulMethods.getAllAccountsForOneService(listWithNotes, serviceName);

        if(accounts.isEmpty()) {
            String possibleVariant = AutoCorrectionServiceName.getOneBestMatch(serviceName, Dictionaries.uniqueServiceNames);

            if(possibleVariant == null) {
                System.out.println("Ничего не найдено. Наиболее подходящие варианты:");
                System.out.println(AutoCorrectionServiceName.getAllBestMatch(serviceName, Dictionaries.uniqueServiceNames));
            }
            else
                System.out.println("Возможно вы имели в виду: " + possibleVariant);

            throw new AccessNotFoundException("Сервис не найден");
        }

        if(serviceLogin == null || serviceLogin.isEmpty())
            return UsefulMethods.sortNoteEntityByServiceName(accounts);

        return List.of( UsefulMethods.getAccountFromServiceByLogin(accounts, serviceName, serviceLogin) );
    }

    private void printNotes(List<NoteEntity> notes) {

        // Управление CMD в Windows
        String up2 = "\033[2A"; // Перемещение курсора вверх на 2 строки от текущей позиции
        String up4 = "\033[4F"; // Перемещение курсора вверх на 4 строки от текущей позиции
        String savePosition = "\033[s"; // Без параметров выполняет операцию сохранения курсора
        String loadPosition = "\033[u"; // Без параметров выполняет операцию курсора восстановления
        String cleanLine = "\033[K"; // Убрать текст из строки

        String dash = "--------------------------------------";

        StringBuilder stringBuilder = new StringBuilder();
        for (NoteEntity note : notes) {
            stringBuilder
                    .append(dash).append("\n")
                    .append(note.toString()).append("\n");
        }
        stringBuilder.append(dash).append("\n");

        // Сохранить позицию курсора в консоли CMD
        stringBuilder.append(savePosition);

        // Вывод в консоль сервисов
        System.out.print(stringBuilder);

        // Задержка
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        StringBuilder hidden = new StringBuilder();
        hidden.append(up2).append(cleanLine).append("Password: *****");
        for(int i = 0; i < notes.size()-1; i++)
            hidden.append(up4).append(cleanLine).append("Password: *****");

        // Скрытие паролей в консоли для сервисов
        System.out.print(hidden);

        // Загрузка позиции курсора в консоли CMD
        System.out.print(loadPosition);
    }

    private void clipboard(NoteEntity note) {

        try {
            StringSelection stringSelection = new StringSelection(note.getPassword(true));

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

//            // Создание ScheduledExecutorService для удаления строки через 30 секунд (30 000 миллисекунд)
//            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//            scheduler.schedule(() -> {
//                // Удаление строки из буфера обмена путем установки пустого содержимого
//                clipboard.setContents(new StringSelection(""), null);
//                scheduler.shutdown(); // Завершаем работу планировщика
//            }, 5, TimeUnit.SECONDS);

            // Создание таймера для удаления строки через 30 секунд (30 000 миллисекунд)

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Удаление строки из буфера обмена путем установки пустого содержимого
                    clipboard.setContents(new StringSelection(""), null);
                    // Закрыть поток таймера (ОБЯЗАТЕЛЬНО). Иначе главный поток не будет закрыт, пока не закрыт timer-поток
                    timer.cancel();
                }
            }, 30_000);

        } catch (KeyStoreException e) {
            System.out.println("Ошибка получения доступа к паролю: " + e.getMessage());
        }

        System.out.println("Пароль к сервису " + note.getServiceName() + " успешно скопироавн в буфер обмена");
    }

}
