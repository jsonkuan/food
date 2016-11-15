package iths.com.food;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jas0n on 2016-11-15.
 */

class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] foodtypes) {
        super(context, R.layout.custom_row, foodtypes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.custom_row, parent, false);

        TextView textView = (TextView) customView.findViewById(R.id.categoryName);
        ImageView imageView = (ImageView) customView.findViewById(R.id.iconThumbnail);

        String singleFoodItem = getItem(position);
        textView.setText(singleFoodItem);

        imageView.setImageResource(getContext().getResources().getIdentifier("img" + (position + 1), "drawable", getContext().getPackageName()));

        return customView;

    }
}
