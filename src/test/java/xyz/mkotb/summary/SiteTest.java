package xyz.mkotb.summary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SiteTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] sites = SITE_TO_ARTICLE.keySet().toArray(new String[SITE_TO_ARTICLE.size()]);
        System.out.println("Select a site to test: ");

        for (int i = 0; i < sites.length; i++) {
            System.out.println((i + 1) + ". " + sites[i]);
        }

        System.out.println((sites.length + 1) + ". Test All\n");

        int selected = scanner.nextInt();

        if (selected == sites.length + 1) {
            testAllSites(sites);
        } else {
            testSite(new Summary(), sites[selected - 1], true);
        }

        scanner.close();
    }

    public static void testAllSites(String[] sites) {
        Summary summary = new Summary();
        boolean success = true;

        for (String site : sites) {
            if (!testSite(summary, site, false)) {
                success = false;
            }

            if (!site.equals(sites[sites.length - 1])) {
                System.out.println("\nPlease wait for next test to start...\n");

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (success) {
            System.out.println("\nCompleted all tests successfully!");
        } else {
            System.out.println("\nSome tests failed! Investigate for more information.");
        }
    }

    public static boolean testSite(Summary summary, String site, boolean ex) {
        System.out.println("Testing " + site + "...");
        List<String> result;

        try {
            result = summary.summaryFor(SITE_TO_ARTICLE.get(site));
        } catch (Exception e) {
            System.out.println("Test for " + site + " failed!");

            if (ex) {
                e.printStackTrace();
            } else {
                System.out.println("Run test for " + site + " in isolation or enter debug mode for more info");
            }
            return false;
        }

        System.out.println("Success for Test " + site + "!\n");

        TestUtils.printSummary(result);
        return true;
    }

    public static final Map<String, String> SITE_TO_ARTICLE = new HashMap<String, String>() {{
        put("The Globe and Mail", "http://www.theglobeandmail.com/news/world/us-politics/donald-trump-refugees-syria-executive-order/article33809191/");

        put("CNN", "http://www.cnn.com/2017/01/27/politics/donald-trump-refugees-executive-order/index.html");

        put("CBC", "http://cbc.ca/news/world/pena-nieto-response-trump-wall-1.3952780");

        put("BBC", "http://bbc.com/news/world-us-canada-38774702");

        put("The Huffington Post", "http://www.huffingtonpost.com/entry/trump-bans-muslims-refugees-executive-order_us_588bcaf3e4b0b065cbbc07ed");

        put("The Washington Post", "http://washingtonpost.com/news/the-fix/wp/2017/01/27/what-is-an-executive-order-and-how-do-president-trumps-stack-up/");

        put("Al Jazeera", "http://aljazeera.com/news/2017/01/trump-signs-order-border-wall-mexico-170125185244055.html");

        put("NPR", "http://www.npr.org/2017/01/28/512055554/trump-signs-a-record-number-of-executive-actions-but-nothing-about-ethics");

        put("Fox News", "http://foxnews.com/politics/2017/01/27/trump-signs-executive-order-for-extreme-vetting-refugees.html");

        put("ABC News", "http://abcnews.go.com/Politics/vice-president-mike-pence-address-march-life-friday/story?id=45076061");

        put("CTV News", "http://ctvnews.ca/world/russia-says-syria-peace-talks-in-geneva-pushed-back-1.3259464");

        put("Global News", "http://globalnews.ca/news/3209844/prenatal-vitamin-a-deficiency-increases-risk-of-alzheimers-later-in-life-canadian-doctors/");

        put("USA Today", "http://www.usatoday.com/story/tech/2014/08/12/girls-who-code-facebook-sheryl-sandberg/13784445/");

        put("AP", "https://www.apnews.com/1be704c5a9cd426299648229272730c2/Trump-orders-strict-new-refugee-screening,-citing-terrorists");

        put("Reuters", "http://www.reuters.com/article/us-usa-trump-refugees-idUSKBN15B2HL");

        put("CBS News", "http://www.cbsnews.com/news/everything-you-need-to-know-about-trumps-executive-actions-so-far/");

        put("Forbes", "https://www.forbes.com/sites/themexicoinstitute/2017/01/23/trump-to-announce-plans-for-renegotiation-of-nafta-five-ways-to-improve-the-agreement/#1c5300df5562");

        put("Real Clear Politics", "http://www.realclearpolitics.com/articles/2017/01/28/mexico_trump_agrees_not_to_talk_publicly_about_wall.html#2");
    }};
}
