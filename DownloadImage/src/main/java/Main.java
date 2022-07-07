import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("target").get();
            Elements elements = doc.select("a").select("img");
            Elements elements2 = doc.select("img");
            elements.addAll(elements2);

            List<String> urlImg = new ArrayList<>(elements.stream()
                    .map(e -> e.absUrl("data-src"))
                    .filter(e -> !e.equals("")).toList()
            );

            System.out.println(urlImg.size());

            urlImg.forEach(element -> {
                try {
                    downloadImage(element);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            urlImg.clear();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        //=============================================================================================
        File file = new File("src/main/images/");
        String[] imageFolder = file.list();

        if (imageFolder != null) {
            printDownloadedImage(imageFolder);
        }

    }

    private static void downloadImage(String src) throws IOException {
        String name = src.substring(src.lastIndexOf("/"));

        URL url = new URL(src);
        InputStream in = url.openStream();
        OutputStream out = new FileOutputStream("src/main/images" + name);

        for (int i; (i = in.read()) != -1;) {
            out.write(i);
        }
        out.close();
        in.close();
    }

    private static void printDownloadedImage(String[] folder) {
        System.out.println("Список названий загруженных изображений:");
        int count = 1;
        for (String imgName : folder) {
            String imgFormat = imgName.substring(imgName.lastIndexOf("."));
            imgName = imgName.substring(0, imgName.lastIndexOf("."));

            System.out.println(count + ") " + "Название: " + imgName);
            System.out.println("\t" + "Формат изображения: " + imgFormat + System.lineSeparator());
            count++;
        }

    }
}
