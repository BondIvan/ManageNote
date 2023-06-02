package Commands;

import Entity.NoteEntity;

import java.util.List;
import java.util.Optional;

import Tools.Tools;

public class TestGet extends Commands {

    private final String postfix;

    public TestGet(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public String perform() throws Exception {
        String[] args = Tools.makeArgsTrue(postfix); // Разбитие postfix-а на состовляющие (конкретные аргументы команды)

        if (postfix.length() == 0)
            throw new UnknownArgsException("Нет параметров");


        if (args.length > 2) // На данный момент не используется аргументов больше чем обычно
            throw new UnknownArgsException("Параметров больше чем нужно");

        return getNote(args).toString();
    }

    private String getNote(String[] args) {

        List<NoteEntity> notes = TestingClass.notes;

        Optional<NoteEntity> optional = notes.stream()
                .filter(note -> note.getIdService().equalsIgnoreCase(args[0]))
                .findFirst();

        if(optional.isEmpty()) {

            Optional<NoteEntity> accounts = optional.or(() -> notes.stream()
                    .filter(note -> note.getIdService().contains("account"))
                    .findAny());

            return accounts.orElseThrow().getIdService();
        }

        return optional.get().getIdService();
    }
}
