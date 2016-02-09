package facade;

import service.CrawlerService;
import service.DownloadService;
import service.FilehippoCrawlerService;

public class Main {

    public static void main(String[] args) {
        CrawlerService crawlerService = new FilehippoCrawlerService();
        DownloadService downloadService = new DownloadService(crawlerService);

        downloadService.download();
    }
}
