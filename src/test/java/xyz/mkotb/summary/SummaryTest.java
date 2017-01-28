package xyz.mkotb.summary;

/**
 * @author Mazen Kotb
 */
public class SummaryTest {
    public static void main(String[] args) {
        System.out.println("Starting Text Summary test...\n");

        TextTest.main(args);

        System.out.println("\nStarting Site Summary tests...");

        SiteTest.testAllSites(SiteTest.SITE_TO_ARTICLE.keySet().toArray(new String[SiteTest.SITE_TO_ARTICLE.size()]));
    }
}
