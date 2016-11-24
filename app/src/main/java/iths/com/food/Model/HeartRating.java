package iths.com.food.Model;

import static iths.com.food.MainActivity.PACKAGE_NAME;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-24.
 */

public class HeartRating {

    private Activity activity;
    private Context context;
    private int healthGrade;
    private int tasteGrade;
    private double averageGrade;

    public HeartRating(Context context, Activity activity) {
        this.context = context;
        this. activity = activity;
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
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= heartNr; i++) {
            int imgId = context.getResources().getIdentifier(healthOrTaste + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.filled_heart);
        }
        setAverageEditGrade();
    }

    public void setHearts(boolean isEditScreen) {

        String edit = "";

        if(isEditScreen) {
            edit = "edit_";
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = activity.getResources().getIdentifier(edit + "heart_health_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= healthGrade; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_health_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.filled_heart);
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_taste_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= tasteGrade; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_taste_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            heart.setImageResource(R.drawable.filled_heart);
        }

        if(isEditScreen) {
            setAverageEditGrade();
        } else {
            setAverageGrade();
        }
    }

    private void setAverageEditGrade() {
        averageGrade = ((double) (healthGrade + tasteGrade) ) / 2;
        TextView averageGradeTV = (TextView) activity.findViewById(R.id.edit_average_number);
        averageGradeTV.setText(Double.toString(averageGrade));
    }

    private void setAverageGrade() {
        averageGrade = ((double) (healthGrade + tasteGrade) ) / 2;
        TextView averageGradeTV = (TextView) activity.findViewById(R.id.average_number);
        averageGradeTV.setText(Double.toString(averageGrade));


    }

    public void fillHeart(View view) {
    }
}