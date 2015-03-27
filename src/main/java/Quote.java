/**
 * Created by root on 11.03.15.
 */
public class Quote {

    private String text;
    private String author;
    private String category;
    private String likes;
    private String tags;
    private String url;


    public Quote(String text, String author, String likes, String tags, String url, String category) {
        this.text = text;
        this.author = author;
        this.likes = likes;
        this.tags = tags;
        this.url = url;
        this.category = category;
    }

    public String appendTags(String[] tagArray){

        for (String tag : tagArray)
            appendTag(tag);
        return tags;
    }

    public String appendTag(String tag) {

        if (tags == null)
            tags = tag;
        else
            tags += "|" + tag;

        return tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {

        try {
            int likesCount = Integer.parseInt(likes);
            if (likesCount > 1700)
                category = "popular";
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.likes = likes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
