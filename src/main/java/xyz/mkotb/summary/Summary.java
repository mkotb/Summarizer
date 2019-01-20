package xyz.mkotb.summary;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.atteo.evo.inflector.English;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Summary {
    private final List<String> functionWords = new ArrayList<>();
    private SentenceDetectorME sentenceDetector;
    /**
     * This is a list of sites which require the user agent
     * to not be the one of a desktop for whatever reason.
     *
     * Such as Forbes due to the Quote of the Day.
     */
    private final List<String> specialAgentSites = Collections.unmodifiableList(Arrays.asList("forbes.com"));
    /**
     * This is essentially a map of the predicate for an element to it's corresponding site's domain to
     * detect whether it's part of the article or it's a form of header or a related article link or such.
     * Sadly all of this work has to be done manually as I found out the hard way that NO SITE FOLLOWS THE
     * SAME STANDARD....
     *
     * Like, cnn.com and money.cnn.com don't follow the same structure or standard. It's unbelievable. How
     * do web developers even survive?
     */
    private final Map<String, Predicate<Element>> siteTextPredicates = new HashMap<String, Predicate<Element>>() {{
        put("def", (element) -> element.tagName().equals("p") && element.classNames().isEmpty());

        put("independent.co", (element) -> (element.tagName().equals("p") || element.tagName().equals("span")) && element.classNames().isEmpty()
                && !element.parents().stream().anyMatch((e) -> e.classNames().contains("full-gallery")));

        put("cbs", (element) -> element.tagName().equals("p") && element.classNames().isEmpty()
                && !element.parent().hasClass("updated-time"));

        put("theglobeandmail", (element) -> element.tagName().equals("p") && !isBold(element));

        put("cnn", (element) -> ((element.tagName().equals("div") || element.tagName().equals("p"))
                && element.className().equals("zn-body__paragraph")));

        put("cbc", (element) -> (element.tagName().equals("div") || element.tagName().equals("p"))
                && !element.className().equals("figure-caption")
                && !isBold(element));

        put("bbc", (element) -> (element.tagName().equals("p")));

        put("huffingtonpost", (element) -> element.tagName().equals("p") && !isItalics(element));

        put("washingtonpost", (element) -> element.tagName().equals("p"));

        put("aljazeera", (element) -> (element.tagName().equals("p") || element.tagName().equals("span"))
                && !element.hasClass("ReadMoreContentSeparator")
                && !element.id().equals("article-topics"));

        put("npr", (element) -> element.tagName().equals("p") && element.classNames().isEmpty()
                && !element.parent().hasClass("caption"));

        put("abcnews.go", (element) -> element.tagName().equals("p")
                && element.hasAttr("itemprop") && element.attr("itemprop").equals("articleBody"));

        /*put("smh.com", (element) -> element.tagName().equals("p") && element.className().isEmpty()
                && !isBold(element)
                && !element.parent().hasClass("story__headline"));*/

        put("nytimes", (element) -> element.tagName().equals("p")
                && element.hasClass("story-content")
                && element.hasClass("story-body-text"));

        put("vox", (element) -> element.tagName().equals("p"));

        put("time", (element) -> element.tagName().equals("p"));

        put("yahoo", (element) -> element.tagName().equals("p")
                && element.hasAttr("type") && element.attr("type").equals("text"));

        put("newyorker", (element) -> element.tagName().equals("p") && element.hasAttr("word_count"));

        put("thehill", (element) -> (element.tagName().equals("p") || element.tagName().equals("div")) && element.classNames().isEmpty());
    }};
    /**
     * This is a map of a domain to it's corresponding function which will get the element(s) which represent
     * the article's contents... Again, has to be done manually because of you know why.
     */
    private final Map<String, Function<Element, Iterable<Element>>> siteArticleFunctions = new HashMap<String, Function<Element, Iterable<Element>>>() {{
        put("def", (body) -> body.getElementsByAttributeValue("itemprop", "articleBody"));
        put("ctvnews", (body) -> body.getElementsByClass("articleBody"));
        put("cbc", (body) -> body.getElementsByClass("story-content"));
        put("theglobeandmail", (body) -> body.getElementsByAttributeValue("class", "x140x460 clearfix"));
        put("bbc", (body) -> body.getElementsByAttributeValue("property", "articleBody"));
        put("huffingtonpost", (body) -> body.getElementsByClass("entry__body"));
        put("aljazeera", (body) -> Collections.singleton(body.getElementById("article-body")));
        put("npr", (body) -> Collections.singleton(body.getElementById("storytext")));
        put("foxnews", (body) -> body.getElementsByClass("article-body"));
        put("abcnews.go", (body) -> body.getElementsByClass("article-copy"));
        put("apnews", (body) -> body.getElementsByClass("articleBody"));
        put("reuters", (body) -> Collections.singleton(body.getElementById("article-text")));
        put("realclearpolitics", (body) -> body.getElementsByClass("article-body-text"));
        put("nytimes", (body) -> Collections.singleton(body.getElementById("story")));
        put("vox", (body) -> body.getElementsByClass("c-entry-content"));
        put("politico", (body) -> body.getElementsByClass("story-text"));
        put("businessinsider", (body) -> Collections.singleton(body.getElementById("content")));
        put("time", (body) -> body.getElementsByTag("article"));
        put("yahoo", (body) -> Collections.singleton(body.getElementById("Main")));
        put("telegra", (body) -> Collections.singleton(body.getElementById("_tl_editor")));
        put("qz", (body) -> body.getElementsByClass("item-body"));
        put("thehill", (body) -> body.getElementsByAttributeValue("property", "content:encoded"));
        put("washingtontimes", (body) -> body.getElementsByClass("article-text"));
    }};

    /**
     * Tests if the element itself is bold or if it any of it's children are bold.
     * @param element element to test
     * @return if it's bold or it's children are
     */
    private boolean isBold(Element element) {
        return (element.tagName().equals("strong") || element.tagName().equals("b"))
                || element.children().stream().anyMatch(this::isBold);
    }

    /**
     * Tests if the element itself is italics or if it any of it's children are italics.
     * @param element element to test
     * @return if it's italics or it's children are
     */
    private boolean isItalics(Element element) {
        return (element.tagName().equals("em") || element.tagName().equals("i"))
                || element.children().stream().anyMatch(this::isItalics);
    }

    Summary() {
        new BufferedReader(new InputStreamReader(Summary.class.getResourceAsStream("/function_words.txt")))
                .lines().forEach(functionWords::add);

        try {
            sentenceDetector = new SentenceDetectorME(new SentenceModel(new File("en-sent.bin")));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Could not load the sentence detector! Shutting down...");
            System.exit(127);
        }
    }

    public static void main(String[] args) throws Exception {
        new SummaryBot(new Summary(), System.getenv("TELEGRAM_KEY"));
    }

    public List<String> summaryFor(String link) throws IOException {
        link = link.replaceAll("http(s)?://(w{3}\\.)?(m|\\Qmobile\\E)\\.", "http://www.");

        StringBuilder builder = new StringBuilder();
        String linkHost = new URL(link).getHost().replace("www.", "");
        Connection.Response response = Jsoup.connect(link)
                // get the desktop version of the site (in most cases)
                // tried using a custom user agent indicating the actual purpose
                // however some sites treat that as a mobile client, resulting in filters failing
                .userAgent(specialAgentSites.contains(linkHost) ? "Summarizer Telegram Bot" :
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4")
                .header("Accept", "*/*")
                .followRedirects(true)
                .execute();
        String hos = response.url().getHost().replace("www.", "");
        Element body = Jsoup.parse(response.body()).body();

        if (hos.startsWith("edition.cnn.com")) {
            hos = "cnn.com";
        }

        String host = hos.substring(0, hos.lastIndexOf('.')); // the host without the [g]TLD

        // site article functions essentially are the means
        // the algorithm uses to find the article body element(s)
        siteArticleFunctions.getOrDefault(host, siteArticleFunctions.get("def")).apply(body)
                .forEach((e) -> addToBuilder(host, e, builder));

        return summaryFor(builder.toString(), 4, true);
    }

    private void addToBuilder(String domain, Element e, StringBuilder b) {
        if (e == null || e.children() == null) {
            return;
        }

        e.children().forEach((element) -> {
            if (!element.hasClass("EmbeddedTweet")) { // ensure this isn't a tweet
                /*
                 * Check if there's an applicable site predicate that checks
                 * if the element is actually part of the article body
                 */
                if (siteTextPredicates.getOrDefault(domain, siteTextPredicates.get("def")).test(element)) {
                    element.childNodes().forEach((node) -> {
                        /*
                         * We only take text and the underlying text of links.
                         *
                         * This filters against span elements or deeper levels
                         * within the element that we are not looking to capture
                         * as they are irrelevant to the article body.
                         */
                        if (node instanceof TextNode) {
                            String text = ((TextNode) node).text();
                            b.append(text);
                        }

                        if (node instanceof Element) {
                            if (((Element) node).tagName().equals("a") && !((Element) node).hasClass("ra-related")) {
                                String text = ((Element) node).text();
                                b.append(text);
                            }
                        }

                        // add a space to the end if there isn't one already
                        if (!b.toString().endsWith(" ")) {
                            b.append(" ");
                        }
                    });
                }
            }

            // check the children of this element
            addToBuilder(domain, element, b);
        });
    }

    // NPR
    public List<String> summaryFor(String text, int length, boolean news) {
        List<String> sentences = Arrays.stream(sentenceDetector.sentDetect(text))
                .distinct() // don't allow duplicate sentences
                .map((sentence) -> {
                    /*
                     * Remove any characters at the beginning of
                     * the sentence that aren't valid. This only
                     * allows for letters of the alphabet, and
                     * beginning speech quotes to start a sentence
                     */
                    while (!isValidStarterChar(sentence.charAt(0))) {
                        sentence = sentence.substring(1);
                    }

                    return sentence;
                })
                .filter((sentence) -> sentence.split("\\W+").length > 5) // sentences must be longer than 5 words
                .collect(Collectors.toList());

        sentences.replaceAll((string) -> string.replace("\n", "")); // remove any new lines from the sentences
        long sentenceLengthSum = 0;

        for (String sentence : sentences) {
            sentenceLengthSum += sentence.split("\\W+").length;
        }

        int sentenceLengthAvg = Math.round(sentenceLengthSum / sentences.size()); // calculate average length
        Map<String, Integer> wordOccurrence = new HashMap<>();

        /*
         * Split the whole text into words, make
         * them singular and map them by occurrence.
         */
        for (String word : text.split("\\W+")) {
            // ignore any function words
            if (functionWords.contains(word.toUpperCase())) {
                continue;
            }

            // remove any non alphanumeric characters
            word = word.toLowerCase().replaceAll("[^a-zA-Z\\d:]", "");
            // get the singular form of the word
            String singular = English.plural(word, 1);

            if (!wordOccurrence.containsKey(singular)) {
                wordOccurrence.put(singular, 0);
            }

            wordOccurrence.put(singular, wordOccurrence.get(singular) + 1);
        }

        /*
         * Map all sentences to their amount of points
         * based on the pointsFor algorithm
         */
        Map<String, Integer> sentenceScoreboard = new HashMap<>();

        sentences.forEach((sentence) -> sentenceScoreboard.put(sentence, pointsFor(sentence, wordOccurrence, sentenceLengthAvg)));

        if (news) {
            // give extra score to headline
            sentenceScoreboard.put(sentences.get(0), sentenceScoreboard.get(sentences.get(0)) + 4);
        }

        // get the top third sentences to use as the highest sentences to choose from
        int selectionSize = Math.floorDiv(sentences.size(), 3);

        // make sure it's at least the size of the length
        if (selectionSize < length) {
            selectionSize = length;

            // but not larger than the sentences available
            if (selectionSize >= sentences.size()) {
                selectionSize = sentences.size() - 1;
            }
        }

        List<String> pointsSorted = new ArrayList<>(sentences);

        // sort the sentences by score
        pointsSorted.sort((s1, s2) -> sentenceScoreboard.get(s2) - sentenceScoreboard.get(s1));

        // sublist then sort by chronological order then sublist to match the length
        pointsSorted = pointsSorted.subList(0, selectionSize);
        pointsSorted.sort((s1, s2) -> sentences.indexOf(s1) - sentences.indexOf(s2));
        return pointsSorted.subList(0, (length >= pointsSorted.size()) ? pointsSorted.size() : length);
    }

    /**
     * The amount of points a sentence has is determined by
     * how many of it's non-function words appear in the article
     */
    public int pointsFor(String sentence, Map<String, Integer> wordOccurrence, int sentenceLengthAverage) {
        int points = 0;
        String[] words = sentence.split("\\W+");

        for (String word : words) {
            if (functionWords.contains(word.toUpperCase())) {
                continue;
            }

            word = word.toLowerCase().replaceAll("[^a-zA-Z\\d:]", "");
            String singular = English.plural(word, 1);

            try {
                points += wordOccurrence.get(singular);
            } catch (NullPointerException ex) {
            }
        }

        /*
         * Remove points if it's a quote, the quote has to
         * be extremely important if to be included in the
         * summary
         */
        if (containsQuote(sentence)) {
            points -= 40;
        }

        double halfOfAvg = sentenceLengthAverage / 2;

        /*
         * Penalize sentences which are either short or long of the average.
         */
        if (words.length < (halfOfAvg) || words.length > (sentenceLengthAverage + halfOfAvg)) {
            points -= Math.abs(sentenceLengthAverage - words.length) * 4;
        }

        return points / sentenceLengthAverage;
    }

    /**
     * Returns if the character is valid to be the starter of a sentence.
     */
    private boolean isValidStarterChar(char ch) {
        return ch == '\"' || ch == '“' || Character.isAlphabetic(ch);
    }

    private boolean containsQuote(String sentence) {
        return (sentence.contains("“") || sentence.contains("”")) ||
                (sentence.indexOf("\"") != sentence.lastIndexOf("\""));
    }
}
