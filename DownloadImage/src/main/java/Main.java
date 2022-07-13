import org.jsoup.Jsoup;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            String folderPath = "src/main/images/";

            ImageDownloader imageDownloader =
                    new ImageDownloader(Jsoup.connect("targetURL").maxBodySize(0).get(), folderPath);
            imageDownloader.downloadImage();

            //===========================================================================================
            File file = new File(folderPath);
            String[] imageFolder = file.list();
            if (imageFolder != null) {
                System.out.println(imageDownloader.printDownloadedImage(imageFolder));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
