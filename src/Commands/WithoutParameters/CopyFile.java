package Commands.WithoutParameters;

import Commands.CommandsWithoutParameters;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.File;
import java.io.IOException;

public class CopyFile extends CommandsWithoutParameters {

    private final String pathToFile; // Путь копируемого файла

    public CopyFile(String path) {
        this.pathToFile = path;
    }

    @Override
    public String perform() throws Exception {

        return copy(pathToFile);
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
