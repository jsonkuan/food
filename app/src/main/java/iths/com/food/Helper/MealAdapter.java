package iths.com.food.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import iths.com.food.Model.Meal;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-15.
 */

public class MealAdapter extends ArrayAdapter<String> {

    DatabaseHelper db;

    public MealAdapter(Context context, ArrayList<String> mealNames) {
        super(context, R.layout.custom_row, mealNames);
    }

    //TODO: REPLACE IMAGEICON WITH IMAGE THUMBNAIL   AND    FIX SCORE AND HEARTS
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.custom_row, parent, false);

        db = new DatabaseHelper(getContext());

        TextView textView = (TextView) customView.findViewById(R.id.categoryName);
        ImageView imageView = (ImageView) customView.findViewById(R.id.iconThumbnail);
        TextView averageScore = (TextView) customView.findViewById(R.id.average_grade_text);
        RatingBar ratingbar = (RatingBar) customView.findViewById(R.id.categoryRatingBar);


        String singleFoodItem = getItem(position);
        Toast.makeText(getContext(), singleFoodItem, Toast.LENGTH_SHORT).show();
        Long id = Long.valueOf(singleFoodItem);
        Meal meal = db.getMeal(id);
        Log.d("HEJHEJ", "ID:t är: "+id);
        textView.setText(meal.getName());

        /*float averageScoreFloat = (float) db.getCategory(singleFoodItem).getAverageScore();

        if (Float.isNaN(averageScoreFloat)) {
            averageScore.setText("Score: ---");
        } else {
            averageScore.setText("Score:  " + averageScoreFloat);
        } */
        averageScore.setText("Score: "+meal.getTotalScore());
        ratingbar.setRating((float)meal.getTotalScore());

        imageView.setImageResource(getContext().getResources().getIdentifier("img" + (position + 1), "drawable", getContext().getPackageName()));

        db.close();

        return customView;

    }
}
