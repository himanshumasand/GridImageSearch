package himanshumasand.github.com.gridimagesearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Himanshu on 9/20/2015.
 */
public class SearchResult {

    private String url;

    public SearchResult(String url) {

        this.url = url;
    }

    public SearchResult (JSONObject result) {
        try {
            this.url = result.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() { return url; }


}
