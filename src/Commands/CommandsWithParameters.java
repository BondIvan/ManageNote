package Commands;

public abstract class CommandsWithParameters {

    //TODO Не знаю как избежать нарушения принципа interface segregation,
    // поэтому в каждом классе есть метод, который возвращает null (метод perform() перегружен)

    // Во всех командах это проверка введённых аргументов и выполнение самой команды
    // В параметре должен быть только postfix введённой команды
    public abstract String perform(String postfix) throws Exception;

}
