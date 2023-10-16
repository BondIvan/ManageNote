package Commands.WithoutParameters;

import Commands.Commands;
import OptionsExceptions.WrongPostfixMethodException;
import Source.StartConsole;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.File;
import java.io.IOException;

public class CopyFile extends Commands {

    private final String pathToTheCopiedFile = StartConsole.PATH; // Путь копируемого файла

    @Override
    public String perform() throws Exception {

        return copy(pathToTheCopiedFile);
    }

    @Override
    public String perform(String postfix) throws Exception {
        throw new WrongPostfixMethodException("У класса " + getClass().getName() + " вызван неправильный метод perform()");
    }

    public String copy(String pathToFile) {

        File file = new File(pathToFile);

        if(file.exists()) {
            Transferable transferable = new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{ DataFlavor.javaFileListFlavor };
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(DataFlavor.javaFileListFlavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    return java.util.Collections.singletonList(file);
                }
            };

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferable, null);

            return "Файл " + file.getName() + " был успешно скопирован в буфер обмена";
        }

        return "Файл не найден";
    }

}
