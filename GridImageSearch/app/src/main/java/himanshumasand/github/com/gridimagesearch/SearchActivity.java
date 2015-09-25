package himanshumasand.github.com.gridimagesearch;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class SearchActivity extends ActionBarActivity implements SearchSettingsDialog.SearchSettingsDialogListener{

    public static final String URL_GET_RESULTS = "https://ajax.googleapis.com/ajax/services/search/images";
    public static final int NUM_RESULTS_PER_PAGE = 8;

    private String query;
    private SearchSettings settings = new SearchSettings();

    ArrayList<String> recentSearches;
    ArrayAdapter<String> recentSearchesAdapter;

    private TextView tvRecentSeachesHeader;
    private ListView lvRecentSearches;
    private GridView gvResults;
    private ArrayList<SearchResult> searchResults;
    private SearchResultsAdapter searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tvRecentSeachesHeader = (TextView) findViewById(R.id.tvRecentsHeader);
        tvRecentSeachesHeader.setVisibility(View.INVISIBLE);
        fetchBackgroundImage();
    }

    private void setupViews() {
        setupGridView();
        setupListView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                // perform query here
                query = q;
                fetchImageResults(0);
                addToRecentSearches();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_clear_search_history:
                clearSearchHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupGridView() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        searchResults = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(this, searchResults);
        gvResults.setAdapter(searchResultsAdapter);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchImageResults(page);
            }
        });
        gvResults.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent imageIntent = new Intent(SearchActivity.this, ImageDetailsActivity.class);
                        imageIntent.putExtra("image", (SearchResult) gvResults.getItemAtPosition(position));
                        startActivityForResult(imageIntent, 1);
                    }
                }
        );
    }

    private void setupListView() {
        tvRecentSeachesHeader.setVisibility(View.VISIBLE);
        lvRecentSearches = (ListView) findViewById(R.id.lvRecents);
        readItems();
        recentSearchesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, recentSearches);
        lvRecentSearches.setAdapter(recentSearchesAdapter);

        if(recentSearches.size() <= 0) {
            hideRecentSearches();
        }
    }

    private void  fetchImageResults(int page) {
        if(page == 0) {
            searchResultsAdapter.clear();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity"); // disable gzip
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("q", query);
        params.put("rsz", String.valueOf(NUM_RESULTS_PER_PAGE));
        params.put("start", String.valueOf(page * NUM_RESULTS_PER_PAGE));
        if(settings != null) {
            params.put("imgsz", settings.getSizeParameter());
            params.put("imgcolor", settings.getColorParameter());
            params.put("imgtype", settings.getTypeParameter());
            params.put("as_sitesearch", settings.site);
        }
        client.get(URL_GET_RESULTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", "RECEIVED: Data for search query: " + response.toString());
                JSONArray resultsJSON = null;
                searchResults = new ArrayList<>();
                try {
                    if (response.getInt("responseStatus") == 200 && response.getJSONObject("responseData") != null && response.getJSONObject("responseData").has("results") && response.getJSONObject("responseData").getJSONArray("results") != null) {
                        resultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                        for (int i = 0; i < resultsJSON.length(); i++) {
                            searchResults.add(new SearchResult(resultsJSON.getJSONObject(i)));
                        }
                    } else {
                        Log.i("DEBUG", "ERROR " + response.getInt("responseStatus") + ": Response data is null. Reason: " + response.getString("responseDetails"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideRecentSearches();
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

    private void  fetchBackgroundImage() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity"); // disable gzip
        RequestParams params = new RequestParams();
        params.put("v", "1.0");
        params.put("q", "Scenery");
        params.put("rsz", 1);
        params.put("imgsz", "xxlarge");
        Random r = new Random();
        int rand = r.nextInt(50 - 1) + 1;
        params.put("start", rand);
        Log.i("DEBUG", "start = " + rand);
        client.get(URL_GET_RESULTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", "RECEIVED: Data for search query: " + response.toString());
                JSONArray resultsJSON = null;
                try {
                    if (response.getInt("responseStatus") == 200 && response.getJSONObject("responseData") != null && response.getJSONObject("responseData").has("results") && response.getJSONObject("responseData").getJSONArray("results") != null) {
                        resultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                        String bgImageUrl = resultsJSON.getJSONObject(0).getString("url");
                        loadImageFromPicasso(bgImageUrl);
                    } else {
                        Log.i("DEBUG", "ERROR " + response.getInt("responseStatus") + ": Response data is null. Reason: " + response.getString("responseDetails"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Log Error
                Log.i("DEBUG", "ERROR: " + responseString);
            }
        });
    }

    public void openSettings() {
        FragmentManager fm = getSupportFragmentManager();
        SearchSettingsDialog searchSettingsDialog = SearchSettingsDialog.newInstance(settings);
        searchSettingsDialog.show(fm, "fragment_settings");
    }

    @Override
    public void onSettingsChange(SearchSettings newSettings) {
        settings = newSettings;
        fetchImageResults(0);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File recentSearchesFile = new File(filesDir, "searches.txt");
        try {
            recentSearches = new ArrayList<String>(FileUtils.readLines(recentSearchesFile));
        } catch (IOException e) {
            recentSearches = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File recentSearchesFile = new File(filesDir, "searches.txt");
        try {
            FileUtils.writeLines(recentSearchesFile, recentSearches);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToRecentSearches() {
        if(!recentSearches.contains(query)) {
            recentSearches.add(query);
            recentSearchesAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void clearSearchHistory() {
        recentSearches.clear();
        recentSearchesAdapter.notifyDataSetChanged();
        hideRecentSearches();
        writeItems();
    }

    private void hideRecentSearches() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlRecentSearches);

        if(rl != null) {
            rl.setVisibility(View.INVISIBLE);
        }

//        TextView tvRecentSeachesHeader = (TextView) findViewById(R.id.tvRecentsHeader);
//        if(tvRecentSeachesHeader != null) {
//            tvRecentSeachesHeader.setVisibility(View.INVISIBLE);
//        }
//        if(lvRecentSearches != null) {
//            lvRecentSearches.setVisibility(View.INVISIBLE);
//        }

    }

    private void loadImageFromPicasso(final String url) {
        final RelativeLayout layout =(RelativeLayout)findViewById(R.id.background);
        Picasso.with(this).load(url).into(new Target() {
            @Override
            @TargetApi(16)
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                layout.setBackground(new BitmapDrawable(getResources(), bitmap));
                Log.i("DEBUG", "URL: " + url);
                setupViews();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // use error drawable if desired
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // use placeholder drawable if desired
            }
        });
    }
}
