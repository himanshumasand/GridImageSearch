package himanshumasand.github.com.gridimagesearch;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Himanshu on 9/20/2015.
 */
public class SearchResult implements Parcelable {

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
            this.pageUrl = result.getString("originalContextUrl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { return title; }

    public String getTbUrl() { return tbUrl; }

    public String getUrl() { return url; }

    public String getPageUrl() { return pageUrl; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.tbUrl);
        dest.writeString(this.url);
        dest.writeString(this.pageUrl);
    }

    protected SearchResult(Parcel in) {
        this.title = in.readString();
        this.tbUrl = in.readString();
        this.url = in.readString();
        this.pageUrl = in.readString();
    }

    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
