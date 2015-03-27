import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 11.03.15.
 */
public class ParserThread extends Thread {


    private static final String URL = "https://www.goodreads.com/quotes/";
    private final AtomicInteger atomicCounter;
    private static long time = 70;
    private static final ReentrantLock lock = new ReentrantLock();
    private static Set<Integer> quotesNumSets = Collections.synchronizedSet(new HashSet<Integer>());

    static {
        Pattern pattern = Pattern.compile("(?<=;\"https://www.goodreads.com/quotes/)(\\d*)(?=(.*?);)");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("/home/hideo/IdeaProjects/ParserGooodReads/src/main/java/quotes0.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String quoteNumString = matcher.group();
                int quoteNum = Integer.parseInt(quoteNumString);
                quotesNumSets.add(quoteNum);
            }
        }
    }

    public ParserThread(AtomicInteger atomicCounter) {

        this.atomicCounter = atomicCounter;


    }

    @Override
    public void run() {


        while (true) {

            int i = atomicCounter.incrementAndGet();
            while (!quotesNumSets.add(i))
                i = atomicCounter.incrementAndGet();

            boolean successful = false;

            while (!successful) {

                lock.lock();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                lock.unlock();

                try {
                    Document document = getDocument(URL + i);
                    Quote quote = new Parser().parse(document);
                    try {
                        if (quote != null){
                            Main.quotesQueue.put(quote);
                            Main.quoteParsedCounter++;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    successful = true;

                    time = 70;
               /* System.out.println(URL + i +"\n\n\n\n");
                System.out.println(new Gson().toJson(quoteText));
                System.out.println("___________________________________\n\n\n\n");*/


                }
                catch (SocketTimeoutException e){
                    e.printStackTrace();
                    successful = true;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    time = 1000 * 10;
                }
            }
        }
    }


    private Document getDocument(String url) throws IOException {

        Connection.Response response = Jsoup.connect(url)
                .timeout(1000 * 10)
                .followRedirects(false)
                .execute();

        String location = response.header("location");


        if (response.statusCode() == 200) location = url;

        if (location.contains(url))
            return Jsoup.connect(location)
                    .followRedirects(false)
                    .get();

        return null;
    }
}
