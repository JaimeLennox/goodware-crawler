package service;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilehippoCrawlerService implements CrawlerService {

    private static final String POPULAR = "http://filehippo.com/popular/";

    public List<String> extractDownloadLinks() {
        List<String> popularSoftware = getPopularSoftwarePages();
        System.out.println(popularSoftware);
        List<String> historyPages = popularSoftware.stream().map(this::getHistoryPage).filter(Objects::nonNull).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        System.out.println(historyPages);
        List<List<String>> versionPages= historyPages.stream().map(this::getVersionPages).collect(Collectors.toList());
        System.out.println(versionPages);

        return versionPages.stream().flatMap(l -> l.stream().map(this::getDownloadLink)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> getPopularSoftwarePages() {
        List<String> popularUrls = IntStream.rangeClosed(1, 10)
                        .mapToObj(n -> POPULAR + Integer.toString(n))
                        .collect(Collectors.toList());

        return popularUrls.stream().flatMap(url -> {
            try {
                return Jsoup.connect(url).get()
                        .getElementsByClass("program-entry-download-link")
                        .select("[href]").stream()
                        .map(e -> e.attr("abs:href"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String getHistoryPage(String softwarePage) {
        try {
            return Jsoup.connect(softwarePage).get()
                    .getElementsByClass("version-history-all-link")
                    .select("a[href]")
                    .attr("abs:href");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getVersionPages(String historyPage) {
        try {
            return Jsoup.connect(historyPage).get()
                    .getElementById("program-history-list")
                    .select("li").stream()
                    .map(li -> li.select("a[href]").attr("abs:href"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getDownloadLink(String versionPage) {
        try {
            return Jsoup.connect(
                    Jsoup.connect(versionPage).get()
                            .getElementsByClass("program-header-download-link-container")
                            .select("a[href]")
                            .attr("abs:href")).get()
                    .getElementById("download-link")
                    .select("a[href]")
                    .attr("abs:href");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
