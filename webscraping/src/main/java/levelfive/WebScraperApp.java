package levelfive;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WebScraperApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========= JSoup Web Scraper ==========");
        System.out.print("Enter Website URL: ");
        String url = scanner.nextLine().trim();

        try {
            Document document = connectToWebsite(url);
            StringBuilder report = new StringBuilder();
            extractTitle(document, report);
            extractMetaDescription(document, report);
            extractHeadings(document, report);
            extractLinks(document, report);
            System.out.println(report);
            saveReport(report.toString());
            System.out.println("\nData saved to scraped_data.txt");

        } catch (IOException exception) {
            System.out.println("Unable to connect to the website.");
            System.out.println(exception.getMessage());
        }
        scanner.close();
    }

    private static Document connectToWebsite(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();
    }

    private static void extractTitle(Document document, StringBuilder report) {
        report.append("========== Website Information ==========\n\n");
        report.append("Title:\n");
        report.append(document.title()).append("\n\n");
    }

    private static void extractMetaDescription(Document document,
                                               StringBuilder report) {

        Element description =
                document.selectFirst("meta[name=description]");

        report.append("Meta Description:\n");

        if (description != null) {
            report.append(description.attr("content"));
        } 
        else {
            report.append("Not Available");
        }
        report.append("\n\n");
    }

    private static void extractHeadings(Document document,
                                        StringBuilder report) {

        report.append("H1 Headings:\n");

        Elements headings = document.select("h1");

        if (headings.isEmpty()) {
            report.append("No H1 headings found.\n");
        } 
        else {
            for (Element heading : headings) {
                report.append("- ")
                        .append(heading.text())
                        .append("\n");

            }
        }
        report.append("\n");
    }

    private static void extractLinks(Document document,
                                     StringBuilder report) {

        Elements links = document.select("a[href]");
        report.append("First 10 Links:\n");
        int count = 0;

        for (Element link : links) {
            report.append(link.absUrl("href")).append("\n");
            count++;
            if (count == 10) {
                break;
            }

        }

        report.append("\n");
        report.append("Total Links Found: ")
                .append(links.size())
                .append("\n");
    }

    private static void saveReport(String report) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter("scraped_data.txt"))) {

            writer.write(report);

        } catch (IOException exception) {
            System.out.println("Unable to save report.");

        }

    }

}