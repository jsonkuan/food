package iths.com.food;

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

import iths.com.food.Helper.DatabaseHelper;

/**
 * Created by wilhelmfors on 25/11/16.
 */

public class MealAdapter extends ArrayAdapter<String> {

        DatabaseHelper db = new DatabaseHelper(getContext());

        MealAdapter(Context context, ArrayList<String> idsOfMeals) {
            super(context, R.layout.custom_row, idsOfMeals);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            View customView = inflator.inflate(R.layout.custom_row, parent, false);

            TextView textView = (TextView) customView.findViewById(R.id.categoryName);
            ImageView imageView = (ImageView) customView.findViewById(R.id.iconThumbnail);
            TextView averageGrade = (TextView) customView.findViewById(R.id.average_grade_text);
            RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.categoryRatingBar);

            String mealID = getItem(position);
            long idOfMeal = Long.valueOf(mealID);
            textView.setText(db.getMeal(idOfMeal).getName());

            float mealScore = (float) db.getMeal(idOfMeal).getTotalScore();

            if (Float.isNaN(mealScore)) {
                averageGrade.setText("No entries");
            } else {
                averageGrade.setText("Average score: " + mealScore);
            }

            ratingBar.setRating((float)db.getMeal(idOfMeal).getTotalScore());

            imageView.setImageResource(getContext().getResources().getIdentifier("img" + (position + 1), "drawable", getContext().getPackageName()));

            return customView;

        }
    }

/*
        ArrayList<String> idsOfMeals = new ArrayList<>();
        ArrayList<Meal> meals = getMeals();
        for (int i = 0; i < meals.size(); i++) {
        idsOfMeals.add(meals.get(i).getId());
        }

        listAdapter = new MealAdapter(this, idsOfMeals);
        ListView listView = (ListView) findViewById(R.id.categoryListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String foodtypes = String.valueOf(adapterView.getItemAtPosition(i));
        Toast.makeText(MealCategoryActivity.this, "" + foodtypes + "is nr " + (i + 1) + "on this!", Toast.LENGTH_SHORT).show();
        }
        }
        ); */
