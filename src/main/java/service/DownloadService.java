package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DownloadService {

    private CrawlerService crawler;

    public DownloadService(CrawlerService crawler) {
        this.crawler = crawler;
    }

    public void download() {
        List<String> downloadLinks = crawler.extractDownloadLinks();

        try {
            Files.write(Paths.get("download.txt"), downloadLinks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
