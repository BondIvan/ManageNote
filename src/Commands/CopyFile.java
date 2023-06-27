package Commands;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyFile extends Commands{

    private final String path; // Путь копируемого файла

    public CopyFile(String path) {
        this.path = path;
    }

    @Override
    public String perform() throws Exception {

        return copy(path);
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
