package himanshumasand.github.com.gridimagesearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Himanshu on 9/20/2015.
 */
public class SearchResult {

    private String title;
    private String tbUrl;
    private String url;
    private String pageUrl;


    public SearchResult(String title, String tbUrl, String url, String pageUrl) {

        this.title = title;
        this.tbUrl = tbUrl;
        this.url = url;
        this.pageUrl = pageUrl;
    }

    public SearchResult (JSONObject result) {
        try {
            this.title = result.getString("titleNoFormatting");
            this.tbUrl = result.getString("tbUrl");
            this.url = result.getString("url");
            this.pageUrl = result.getString("visibleUrl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { return title; }

    public String getTbUrl() { return tbUrl; }

    public String getUrl() { return url; }

    public String getPageUrl() { return pageUrl; }

}
