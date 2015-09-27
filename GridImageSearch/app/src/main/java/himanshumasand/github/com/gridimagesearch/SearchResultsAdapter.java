package himanshumasand.github.com.gridimagesearch;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Himanshu on 9/16/2015.
 */
public class SearchResultsAdapter extends ArrayAdapter<SearchResult> {

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final Random mRandom;

    private static class ViewHolder {
        TextView title;
        DynamicHeightImageView image;
    }

    public SearchResultsAdapter(Context context, List<SearchResult> objects) {
        super(context, 0, objects);
        this.mRandom = new Random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult result = getItem(position);

        final ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_result, parent, false);
            viewHolder.image = (DynamicHeightImageView) convertView.findViewById(R.id.ivSearchResult);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(0);
        Picasso.with(getContext()).load(result.getTbUrl()).into(viewHolder.image);
        viewHolder.image.setHeightRatio(getPositionRatio(position));

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0;
    }
}
