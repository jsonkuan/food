package iths.com.food.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import iths.com.food.Model.MyCamera;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-15.
 */

public class MealAdapter extends ArrayAdapter<String> {

    DatabaseHelper db;

    public MealAdapter(Context context, ArrayList<String> mealIds) {
        super(context, R.layout.custom_row, mealIds);
    }

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
        TextView dateUploaded = (TextView) customView.findViewById(R.id.date_of_meal);


        String mealID = getItem(position);
        Long id = Long.valueOf(mealID);
        Meal meal = db.getMeal(id);
        textView.setText(meal.getName());
        dateUploaded.setText(meal.getDateTime());

        averageScore.setText("Score: "+meal.getTotalScore());
        ratingbar.setRating((float)meal.getTotalScore());


        String imagePath = meal.getImagePath();
        Uri imageUri = Uri.parse(imagePath);
        Bitmap image = MyCamera.rotatePhoto(imageUri.getPath());

        imageView.setImageBitmap(image);

        db.close();

        return customView;

    }
}
