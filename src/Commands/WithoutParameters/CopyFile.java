package Commands.WithoutParameters;

import Commands.Commands;
import Source.StartConsole;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.File;
import java.io.IOException;

public class CopyFile implements Commands {

    private final String pathToTheCopiedFile = StartConsole.PATH; // Путь копируемого файла

    @Override
    public String perform(String postfix) throws Exception {

        return copy(pathToTheCopiedFile);
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
