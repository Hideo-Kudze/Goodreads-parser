import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 10.03.15.
 */
public class Main {

    public static final BlockingQueue<Quote> quotesQueue =new ArrayBlockingQueue<Quote>(100000);
    public static final AtomicInteger quoteNumCounter = new AtomicInteger(152711);
    public static int quoteParsedCounter = 0;

    public static void main(String[] args) throws IOException {

        File orig = new File("/home/hideo/IdeaProjects/ParserGooodReads/src/main/java/quotes0.csv");
        File dest = new File("/home/hideo/IdeaProjects/ParserGooodReads/src/main/java/quotes0_1.csv");
        dest.createNewFile();
        Scanner in = new Scanner(orig, "UTF-8");

        PrintWriter out = null;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(dest, false), Charset.forName("UTF-8"));
            out = new  PrintWriter(new BufferedWriter(outputStreamWriter));
            out.println("\"Quote (Text)\";\"Author\";\"Category\";\"Likes\";\"Tags (seperated by | )\";\"URL\"");

            int i =0;
            while (in.hasNext()){
                i++;
                String line = in.nextLine();
                line = line.replaceAll("\\\"(\\d*)?\";\\\"(\\d*)?\";$", "");
                out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

     /*   for (int i = 0; i < 50; i++) {
            new ParserThread(quoteNumCounter).start();
        }

        new Thread() {
            @Override
            public void run() {

                try(PrintWriter out = new PrintWriter(new FileOutputStream("/home/hideo/IdeaProjects/ParserGooodReads/src/main/java/quoteNum", true))) {


                while (true) {

                    int oldCount = quoteParsedCounter;
                    out.println(quoteNumCounter.get());
                    out.flush();
                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Quotes per minute: " + (quoteParsedCounter - oldCount));
                }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        new WriteToCsv();*/
    }


    private static boolean quoteExists(String url) {

        try {
            Connection.Response response = response = Jsoup.connect(url)
                    .followRedirects(false)
                    .execute();
            if (response.statusCode() == 200) return true;
            String location = response.header("location");
            return location.contains(url);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
