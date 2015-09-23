package himanshumasand.github.com.gridimagesearch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {

    public static final String URL_GET_RESULTS = "https://ajax.googleapis.com/ajax/services/search/images";

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<SearchResult> searchResults;
    private SearchResultsAdapter searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        searchResults = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(this, searchResults);
        gvResults.setAdapter(searchResultsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();
        fetchImageResults(query);
        Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
    }

    private void  fetchImageResults(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity"); // disable gzip
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("q", query);
        params.put("rsz", "8");
        client.get(URL_GET_RESULTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", "RECEIVED: Data for search query: " + response.toString());
                JSONArray resultsJSON = null;
                searchResults = new ArrayList<>();
                try {
                    if(response.getInt("responseStatus") == 200 && response.getJSONObject("responseData") != null && response.getJSONObject("responseData").has("results") && response.getJSONObject("responseData").getJSONArray("results") != null) {
                        resultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                        for(int i = 0; i < resultsJSON.length(); i++) {
                            searchResults.add(new SearchResult(resultsJSON.getJSONObject(i)));
                        }
                    }
                    else {
                        Log.i("DEBUG", "ERROR " + response.getInt("responseStatus") +": Response data is null. Reason: " + response.getString("responseDetails"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                searchResultsAdapter.clear();
                searchResultsAdapter.addAll(searchResults);
                searchResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Log Error
                Log.i("DEBUG", "ERROR: " + responseString);
            }
        });
    }


}
