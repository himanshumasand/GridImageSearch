package himanshumasand.github.com.gridimagesearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ImageDetailsActivity extends ActionBarActivity {

    private SearchResult image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);


        ImageView ivImage = (ImageView) findViewById(R.id.ivImageLarge);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);

        image = getIntent().getParcelableExtra("image");
        Picasso.with(this).load(image.getUrl()).into(ivImage);
        tvTitle.setText(image.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_details, menu);
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


    public void onViewImageButtonClicked(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(image.getUrl()));
        startActivity(i);
    }

    public void onVisitPageButtonClicked(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(image.getPageUrl()));
        startActivity(i);
    }
}
