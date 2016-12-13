package iths.com.food.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import iths.com.food.R;
import iths.com.food.helper.db.DatabaseHelper;


/**
 * Created by jas0n on 2016-11-15.
 *
 */

public class CategoryAdapter extends ArrayAdapter<String> {


    public CategoryAdapter(Context context, ArrayList<String> categories) {
        super(context, R.layout.custom_row, categories);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        String categoryName = getItem(position);
        TextView textView = (TextView) customView.findViewById(R.id.categoryName);
        textView.setText(categoryName);

        DatabaseHelper db = new DatabaseHelper(getContext());
        float averageScoreFloat = (float) db.getCategory(categoryName).getAverageScore();

        TextView averageScore = (TextView) customView.findViewById(R.id.average_grade_text);
        if (Float.isNaN(averageScoreFloat)) {
            averageScore.setText(R.string.average_score_null);
        } else {
            averageScore.setText(String.format("Avg:  %s", averageScoreFloat));
        }

        RatingBar ratingbar = (RatingBar) customView.findViewById(R.id.categoryRatingBar);
        ratingbar.setRating(averageScoreFloat);

        ImageView imageView = (ImageView) customView.findViewById(R.id.iconThumbnail);
        imageView.setImageResource(getContext().getResources().getIdentifier("img" + db.getCategory(categoryName).getIconId(), "drawable", getContext().getPackageName()));

        db.close();

        return customView;
    }
}
