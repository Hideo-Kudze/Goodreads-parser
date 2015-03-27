
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 12.03.15.
 */

public class WriteToCsv extends Thread {

    private AtomicInteger counter = new AtomicInteger();
    private int fileNum = 0;


    public WriteToCsv() {

        new Thread() {
            @Override
            public void run() {


                while (true) {

                    int oldCount = counter.get();

                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Quotes write per minute: " + (counter.get() - oldCount));
                    System.out.println("Total write count: " + counter.get());
                }

            }
        }.start();

        start();
    }


    @Override
    public void run() {


        PrintWriter out = null;
        try {
            out = getPrintWriter();

            while (true)
                try {
                    Quote quote = Main.quotesQueue.poll(1000, TimeUnit.HOURS);

                    if (counter.get() == 1000000) {
                        out.close();
                        out = getPrintWriter();
                    }

                    writQuoteToCsv(quote, out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    private PrintWriter getPrintWriter() throws IOException {
        String path = "/home/hideo/IdeaProjects/ParserGooodReads/src/main/java/";
        String fileName = "quotes" + fileNum + ".csv";
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path + fileName,true), Charset.forName("UTF-8"));
        return new PrintWriter(new BufferedWriter(out));
    }

    public void writQuoteToCsv(Quote quote, PrintWriter out) throws IOException {


        String text = quote.getText();
        String author = quote.getAuthor();
        String category = quote.getCategory();
        String likes = quote.getLikes();
        String tags = quote.getTags();
        String url = quote.getUrl();

        print(out, text);
        print(out, author);
        print(out, category);
        print(out, likes);
        print(out, tags);
        print(out, url);


        out.println();
        out.flush();
        counter.incrementAndGet();
    }

    private void print(PrintWriter out, String text) throws IOException {

        text = text.replaceAll("\"", "\"\"");
        text = "\"" + text + "\"";
        out.print(text + ";");
    }
}
