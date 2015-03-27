import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by root on 11.03.15.
 */

public class Parser {

    public Quote parse(Document document){

        if (document == null)
            return null;

        String quoteText = getQuoteText(document);
        String author = getAuthor(document);
        String likes = getLikes(document);
        String tags = getTags(document);
        String url = document.baseUri();
        String category = tags.split("|")[0];

        return  new Quote(quoteText, author, likes, tags, url, category);

    }

    private String getQuoteText(Document document) {

        Elements quoteTextElement = document.select("h1.quoteText");
        return getTextWithNewLines(quoteTextElement.first());
    }

    private String getTextWithNewLines(Element element){

        String htmlWithReplacedBr = element.toString().replaceAll("(?i)<br[^>]*>", "br2n");
        String text = Jsoup.parse(htmlWithReplacedBr).text();
        return text.replaceAll("br2n", "\n");
    }

    private String getAuthor(Document document){

        Elements authorElement = document.select(".quote.mediumText a.actionLink");
        return authorElement.text();
    }

    private String getLikes(Document document){

        Elements likesElement = document.select(".uitext.smallText");
        String likesString = likesElement.text();
        return likesString.split(" ")[0];
    }

    private String getTags(Document document){

        Elements tagsElement = document.select(".greyText.smallText");
        String tagsString = tagsElement.text();

        tagsString = tagsString.replaceAll("^tags:( )*", "");
        tagsString = tagsString.replaceAll(",( )*", "|");
        return tagsString;
    }
}
