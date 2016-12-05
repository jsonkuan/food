package iths.com.food.Model;

import static iths.com.food.MainActivity.PACKAGE_NAME;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-24.
 */

public class HeartRating {

    private static final String TAG = "LOGTAG";
    private Activity activity;
    private Context context;
    private static int healthGrade;
    private static int tasteGrade;
    private double averageGrade;
    private View layoutView;

    public HeartRating(View layoutView, Context context, Activity activity) {
        this.layoutView = layoutView;
        this.context = context;
        this.activity = activity;
    }

    public void fillHearts(View view) {
        ImageView iv = (ImageView) view;
        ViewGroup viewParent = (ViewGroup) iv.getParent();
        int resid = view.getId();
        String idStr = context.getResources().getResourceEntryName(resid);
        String healthOrTaste;
        int heartNr;

        if( (viewParent).getId() == R.id.edit_health_hearts) {
            healthOrTaste = "edit_heart_health_";
            heartNr = Integer.parseInt(idStr.replace(healthOrTaste, ""));
            healthGrade = heartNr;
        } else {
            healthOrTaste = "edit_heart_taste_";
            heartNr = Integer.parseInt(idStr.replace(healthOrTaste, ""));
            tasteGrade = heartNr;
        }
        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(healthOrTaste + i, "id", PACKAGE_NAME);
            Log.d(TAG, "imgId i fillHearts: " + imgId);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            if(i <= heartNr) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
            heart.setImageResource(R.drawable.empty_heart);
            }
        }
        setAverageGrade(true);
    }

    public void setHearts(boolean isEditScreen, int healthGrade, int tasteGrade) {

        this.healthGrade = healthGrade;
        this.tasteGrade = tasteGrade;

        Log.d(TAG, "Inne i setHearts");
        String edit = "";

        if(isEditScreen) {
            edit = "edit_";
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_health_" + i, "id", PACKAGE_NAME);
            Log.d(TAG, "imgId = " + imgId);
            Log.d(TAG, "imgId 2 = " + context.getResources().getIdentifier("heart_health_1", "id", PACKAGE_NAME));
            ImageView heart = (ImageView) layoutView.findViewById(imgId);
            Log.d(TAG, "Is heart null? " + (heart == null));
            if(i <= healthGrade) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
                heart.setImageResource(R.drawable.empty_heart);
            }
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_taste_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) layoutView.findViewById(imgId);
            if(i <= tasteGrade) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
                heart.setImageResource(R.drawable.empty_heart);
            }
        }

        setAverageGrade(isEditScreen);
    }

    private void setAverageGrade(boolean isEditScreen) {
        TextView averageGradeTV;
        if(isEditScreen) {
            averageGradeTV = (TextView) layoutView.findViewById(R.id.edit_average_number);
        } else {
            averageGradeTV = (TextView) layoutView.findViewById(R.id.average_number);
        }
        Log.d(TAG, "Is averageGradeTV null? " + (averageGradeTV == null));
        Log.d(TAG, "healthGrade = " + healthGrade);
        Log.d(TAG, "tasteGrade = " + tasteGrade);
        averageGrade = ((double) (healthGrade + tasteGrade) ) / 2;
        averageGradeTV.setText(Double.toString(averageGrade));
    }

    public static int getHealthGrade() {
        return healthGrade;
    }
    public static int getTasteGrade() {
        return tasteGrade;
    }
}