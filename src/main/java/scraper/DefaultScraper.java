package scraper;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DefaultScraper implements Scraper {
    private static final String PRICE_SELECTOR = ".detail__info-xlrg";
    private static final String BED_SELECTOR = ".nhs_BedsInfo";
    private static final String BATHS_SELECTOR = ".nhs_BathsInfo";
    private static final String GARAGE_SELECTOR = ".nhs_GarageInfo";

    @Override @SneakyThrows
    public Home scrape(String url) {
        Document doc = Jsoup.connect(url).get();
        int price = getPrice(doc);
        double beds = getBeds(doc);
        double baths = getBaths(doc);
        double garages = getGarages(doc);
        return new Home(price, beds, baths, garages);
    }

    private int getPrice(Document doc) {
        String price = doc.selectFirst(PRICE_SELECTOR).text().replaceAll("[^0-9]", "");
        return Integer.parseInt(price);
    }

    private double getBeds(Document doc) {
        String bedNum = doc.selectFirst(BED_SELECTOR).text().replaceAll("[^0-9.]", "");
        return Double.parseDouble(bedNum);
    }

    private double getBaths(Document doc) {
        String bathNum = doc.selectFirst(BATHS_SELECTOR).text().replaceAll("[^0-9.]", "");
        return Double.parseDouble(bathNum);
    }

    private double getGarages(Document doc) {
        String garageNum = doc.selectFirst(GARAGE_SELECTOR).text().replaceAll("[^0-9.]", "");
        return Double.parseDouble(garageNum);
    }


}
