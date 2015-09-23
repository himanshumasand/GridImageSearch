package himanshumasand.github.com.gridimagesearch;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import himanshumasand.github.com.gridimagesearch.R;
import himanshumasand.github.com.gridimagesearch.SearchResult;

/**
 * Created by Himanshu on 9/16/2015.
 */
public class SearchResultsAdapter extends ArrayAdapter<SearchResult> {

    private static class ViewHolder {
        ImageView image;
    }

    public SearchResultsAdapter(Context context, List<SearchResult> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult result = getItem(position);

        final ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_result, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivSearchResult);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(0);
        Picasso.with(getContext()).load(result.getUrl()).into(viewHolder.image);

        return convertView;
    }
}
