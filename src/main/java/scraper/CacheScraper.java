package scraper;

import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CacheScraper implements Scraper {
    Scraper defaultScraper = new DefaultScraper();

    @Override @SneakyThrows
    public Home scrape(String url) {
        // Create connection to DB
        Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        Statement statement = connection.createStatement();

        // Execute query
        String query = String.format("select count(*) as count from homes where url='%s'", url);
        ResultSet rs = statement.executeQuery(query);

        if (rs.getInt("count") > 0) {
            // Get cached data from db
            System.out.println("Loading cached data from db ...");

            String getQuery = String.format("select * from homes where url='%s'", url);
            rs = statement.executeQuery(getQuery);
            return new Home(rs.getInt("price"),
                            rs.getDouble("beds"),
                            rs.getDouble("bathes"),
                            rs.getDouble("garages"));
        } else {
            // Scrape the data if there is no cache for current url
            System.out.println("Scraping new data from url ...");

            Home home = defaultScraper.scrape(url);
            String insertQuery = String.format("insert into homes(url, price, beds, bathes, garages) "
                                                + "values('%s', %d, %f, %f, %f)",
                                                url,
                                                home.getPrice(),
                                                home.getBeds(),
                                                home.getBaths(),
                                                home.getGarages()
                                        );
            statement.executeUpdate(insertQuery);
            return home;
        }
    }
}
