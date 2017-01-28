package xyz.mkotb.summary;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

public class SummaryBot implements Listener {
    public static final String[] NUMBER_EMOJIS = new String[] {"1⃣", "2⃣", "3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣", "9⃣"};
    private Summary summary;
    private TelegramBot bot;

    public SummaryBot(Summary instance, String key) {
        summary = instance;
        bot = TelegramBot.login(key);
        bot.getEventsManager().register(this);
        bot.startUpdates(false);
        log("Successfully logged in!");
    }

    public void log(String text) {
        System.out.println("[" + bot.getBotUsername() + "] " + text);
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
        List<String> summarization;

        try {
            summarization = summary.summaryFor(event.getQuery().getQuery());
        } catch (MalformedURLException ex) {
            event.getQuery().answer(bot, createArticle("Invalid URL", "Please send a valid link"));
            return;
        } catch (IOException ex) {
            event.getQuery().answer(bot, createArticle("Can't Access", "There was a problem trying to access that website. Sorry!"));
            return;
        } catch (IndexOutOfBoundsException ex) {
            event.getQuery().answer(bot, createArticle("Can't Read Site", "I couldn't find the article body! If this site is on the supported list, contact @MazenK"));
            return;
        } catch (Exception ex) {
            Date date = new Date();
            log("There was an unexpected error trying to summarize the link " + event.getQuery().getQuery() + " at " + date.toString());
            ex.printStackTrace();
            event.getQuery().answer(bot, createArticle("Error", "There was an unexpected error when trying to summarize your link. " +
                    "Contact @MazenK and mention the following timestamp: " + date.toString()));
            return;
        }

        if (summarization.isEmpty()) {
            event.getQuery().answer(bot, createArticle("Couldn't summarize", "I couldn't create a summary! If this site is on the supported list, contact @MazenK"));
            return;
        }

        String message = "[Original Article](" + event.getQuery().getQuery() + ")\n\n" + summaryMessage(summarization);

        event.getQuery().answer(bot, createArticle("Your Summary", message));
    }

    public InlineQueryResultArticle createArticle(String title, String content) {
        return InlineQueryResultArticle.builder()
                .title(title)
                .description(content)
                .id("a").inputMessageContent(InputTextMessageContent.builder().messageText(content)
                        .parseMode(ParseMode.MARKDOWN).disableWebPagePreview(false).build())
                .build();
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        if (event.getCommand().equals("start")) {
            event.getChat().sendMessage(SendableTextMessage.builder()
                    .message("This bot provides summaries to news articles or text!\n\n" +

                            "Currently, the bot natively supports the following news sites for links: " +
                            "The Globe and Mail, CNN, CBC, BBC, The Huffington Post, The Washington Post, Al Jazeera, NPR, Fox News, ABC News, " +
                            "CTV News, Global News, USA Today, AP, Reuters, CBS News, Forbes, The New York Times, Vox, Politico, The Independent, " +
                            "The Guardian, Business Insider, TIME, Yahoo News, The New Yorker, CNBC, The Telegraph, Mirror and Real Clear Politics.\n\n" +

                            "However, with the /summarize_text command, you can send any text and the bot will attempt to summarize it." +
                            " Although the bot supports all kinds of text, the algorithm has been optimized for long text and news articles, " +
                            "so please keep that in mind during usage.\n\n" +

                            "You can summarize a news article by using the /summarize command with a link to the article you wish to summarize as the argument\n\n" +

                            "This bot was created by @MazenK and feel free to contact him for any issues you may encounter.")
                    .build());
            return;
        }

        if (event.getCommand().equals("contribute")) {
            event.getChat().sendMessage("Feel free to contribute to the project here: https://github.com/mkotb/Summarizer");
            return;
        }

        if (event.getCommand().equals("summarize")) {
            if (event.getArgs().length == 0) {
                event.getChat().sendMessage("Please send a link as an argument with the command. Like /summarize [link]");
                return;
            }

            List<String> summarization;

            try {
                summarization = summary.summaryFor(event.getArgs()[0]);
            } catch (MalformedURLException ex) {
                event.getChat().sendMessage("Please send a valid link!");
                return;
            } catch (IOException ex) {
                event.getChat().sendMessage("There was a problem trying to access that website. Sorry!");
                return;
            } catch (IndexOutOfBoundsException ex) {
                event.getChat().sendMessage("I couldn't find the article body! If this site is on the supported list, contact @MazenK");
                return;
            } catch (Exception ex) {
                Date date = new Date();
                log("There was an unexpected error trying to summarize the link " + event.getArgs()[0] + " at " + date.toString());
                ex.printStackTrace();
                event.getChat().sendMessage("There was an unexpected error when trying to summarize your link. " +
                        "Contact @MazenK and mention the following timestamp: " + date.toString());
                return;
            }

            if (summarization.isEmpty()) {
                event.getChat().sendMessage("I couldn't create a summary! If this site is on the supported list, contact @MazenK");
                return;
            }

            sendSummary(summarization, event.getChat(), event.getMessage());
        }

        if (event.getCommand().equals("summarize_text")) {
            if (event.getArgs().length == 0) {
                event.getChat().sendMessage("Please send text as an argument with the command. " +
                        "Text must be at least 7 sentences. Like /summarize [text]");
                return;
            }

            List<String> summarization;

            try {
                summarization = summary.summaryFor(event.getArgsString(), 4, false);
            } catch (IndexOutOfBoundsException ex) {
                event.getChat().sendMessage("There wasn't enough sentences! Please try again! " +
                        "If there were over 7 sentences and you see this message, contact @MazenK");
                return;
            } catch (Exception ex) {
                Date date = new Date();
                log("There was an unexpected error trying to summarize the link " + event.getArgs()[0] + " at " + date.toString());
                ex.printStackTrace();
                event.getChat().sendMessage("There was an unexpected error when trying to summarize your link. " +
                        "Contact @MazenK and mention the following timestamp: " + date.toString());
                return;
            }

            if (summarization.isEmpty()) {
                event.getChat().sendMessage("There wasn't enough sentences! Please try again! " +
                        "If there were over 7 sentences and you see this message, contact @MazenK");
                return;
            }

            sendSummary(summarization, event.getChat(), event.getMessage());
        }
    }

    public void sendSummary(List<String> summary, Chat chat, Message message) {
        chat.sendMessage(SendableTextMessage.builder().replyTo(message).message(summaryMessage(summary)).build());
    }

    public String summaryMessage(List<String> summary) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < summary.size(); i++) {
            builder.append(NUMBER_EMOJIS[i]).append(' ').append(summary.get(i)).append("\n\n");
        }

        builder.setLength(builder.length() - 2);
        return builder.toString();
    }
}
