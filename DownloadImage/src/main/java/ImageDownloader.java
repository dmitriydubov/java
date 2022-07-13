import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ImageDownloader {
    private String destFolder;
    private final Document document;

    //=========================================================================================================
    protected ImageDownloader(Document document, String destFolder) {
        this.document = document;
        this.destFolder = destFolder;
    }

    //=========================================================================================================
    protected void downloadImage() {
        try {
            List<String> elementsSrcAttr = document.select("a").select("img").stream()
                    .map(e -> e.absUrl("src")).toList();

            List<String> elementsDataSrcAttr = document.select("a").select("img").stream()
                    .map(e -> e.absUrl("data-src")).toList();

            List<String> urlImg = Stream.of(elementsSrcAttr, elementsDataSrcAttr)
                    .flatMap(Collection::stream)
                    .filter(e -> !e.equals(""))
                    .toList();

            urlImg.forEach(element -> {
                try {
                    putImageToDestinationFolder(element);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void putImageToDestinationFolder(String src) throws IOException {
        String name = src.substring(src.lastIndexOf("/"));

        URL url = new URL(src);
        InputStream in = url.openStream();
        OutputStream out = Files.newOutputStream(Paths.get(destFolder + name));

        out.write(in.readAllBytes());
        in.close();
        out.close();
    }

    protected String printDownloadedImage(String[] folder) {
        StringBuilder stringBuilder = new StringBuilder();
        String s = System.lineSeparator();
        stringBuilder.append("Список названий загруженных изображений:" + s);

        int count = 1;
        for (String imgName : folder) {
            String imgFormat = imgName.substring(imgName.lastIndexOf("."));
            imgName = imgName.substring(0, imgName.lastIndexOf("."));

            stringBuilder.append(
                    "========================================================================" + s +
                            count + ") " + "Название: " + imgName + s + " ".repeat(4) +
                            "Формат изображения: " + imgFormat + s
            );

            count++;
        }

        return stringBuilder.toString();
    }

    //==========================================================================================================
    protected void setDestFolder(String destFolder) {
        this.destFolder = destFolder;
    }

    protected String getDestFolder() {
        return destFolder;
    }
}
