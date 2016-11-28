package iths.com.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.Date;

import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Meal;

public class MealActivity extends AppCompatActivity {


    private boolean editable;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "TAG";
    private ImageView imageView;
    private static Uri photoFilePath;
    private static boolean isOpenedFromMenu;
    private int healthGrade;
    private int tasteGrade;
    private double averageGrade;
    private boolean savePosition;
    EditText name;
    EditText description;
    DatabaseHelper db;
    long id;
//    Spinner spinner;
//    ArrayList<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Testar logfunktionen");
        db = new DatabaseHelper(getApplicationContext());
//PUTBACK        categories = db.getCategories();
        //setContentView(R.layout.activity_meal_edit);
        if(isOpenedFromMenu) {
            isOpenedFromMenu = false;
            setContentView(R.layout.activity_meal_edit);
//            spinner = (Spinner) findViewById(R.id.spinner);
//            setUpSpinner();
            imageView = (ImageView) findViewById(R.id.edit_meal_image);
            takePhoto(imageView);
            name = (EditText) findViewById(R.id.name);
            description = (EditText) findViewById(R.id.desc);
        } else {
            setContentView(R.layout.activity_meal);
            imageView = (ImageView) findViewById(R.id.meal_image);
            setHearts(false);

        }

    }

    public void makeEditable(View view) {
        setContentView(R.layout.activity_meal_edit);
       // setUpSpinner(); //PUT BACK
        setHearts(true);
    }
//    private void setUpSpinner() {
//        String[] categoryNames = new String[categories.size()];
//        for(int i = 0; i < categories.size(); i++) {
//            categoryNames[i] = categories.get(i).getName();
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryNames);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//    }

    public static void setOpenedFromCameraActivity(boolean b) {
        isOpenedFromMenu = b;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                showImage();
            }
        }
    }

    private File createFile() {
        File dir = getDirectory();
        File file = new File(dir, "iths" + new Date().getTime() + ".jpg");
        return file;
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null) {
            File photo = createFile();
            photoFilePath = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private File getDirectory() {
        File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File ithsDir = new File(photoDir, "ithsPics");
        if(!ithsDir.exists()) {
            if(!ithsDir.mkdirs()) {
                Log.d(TAG, "Could not create directory" + ithsDir.toString());
            }
        }
        return ithsDir;
    }

    private void showImage() {
        imageView = (ImageView) findViewById(R.id.edit_meal_image);
        int imageViewHeight = imageView.getHeight();
        int imageViewWidth = imageView.getWidth();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        int scaleFactor = 1;

        try {
            scaleFactor = Math.min(opt.outHeight / imageViewHeight, opt.outWidth / imageViewWidth);
        } catch(Exception e) {
            Toast.makeText(this, "Funkade inte att skala", Toast.LENGTH_SHORT).show();
        }

        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap image = BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        String stringUri = photoFilePath.toString();

        ExifInterface exif;
        int orientation = 167;

        try {
            exif = new ExifInterface(photoFilePath.getPath());
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("ORIENTATION", orientation + "");
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(), "orientation: " + orientation, Toast.LENGTH_SHORT).show();

        Matrix matrix = new Matrix();
        if(orientation == 6) {
            matrix.postRotate(90);
        } else if(orientation == 8) {
            matrix.postRotate(270);
        } else if(orientation == 3) {
            matrix.postRotate(180);
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        imageView.setImageBitmap(rotatedBitmap);
    }

    /**
     * Fyller i hjärtan när användaren trycker på ett hjärta.
     * @param view
     */
    public void fillHearts(View view) {
        ImageView iv = (ImageView) view;
        ViewGroup viewParent = (ViewGroup) iv.getParent();
        int resid = view.getId();
        String idStr = getResources().getResourceEntryName(resid);

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
            int imgId = getResources().getIdentifier(healthOrTaste + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= heartNr; i++) {
            int imgId = getResources().getIdentifier(healthOrTaste + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
            heart.setImageResource(R.drawable.filled_heart);
        }
        setAverageEditGrade();
    }

    /**
     * Fyller i hjärtan enligt betyg då skärmen laddas.
     * @param isEditScreen
     */

    private void setHearts(boolean isEditScreen) {

        String edit = "";

        if(isEditScreen) {
            edit = "edit_";
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = getResources().getIdentifier(edit + "heart_health_" + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= healthGrade; i++) {
            int imgId = getResources().getIdentifier(edit + "heart_health_" + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
            heart.setImageResource(R.drawable.filled_heart);
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = getResources().getIdentifier(edit + "heart_taste_" + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
            heart.setImageResource(R.drawable.empty_heart);
        }
        for(int i = 1; i <= tasteGrade; i++) {
            int imgId = getResources().getIdentifier(edit + "heart_taste_" + i, "id", getPackageName());
            ImageView heart = (ImageView) findViewById(imgId);
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
        TextView averageGradeTV = (TextView) findViewById(R.id.edit_average_number);
        averageGradeTV.setText(Double.toString(averageGrade));
    }

    private void setAverageGrade() {
        averageGrade = ((double) (healthGrade + tasteGrade) ) / 2;
        TextView averageGradeTV = (TextView) findViewById(R.id.average_number);
        averageGradeTV.setText(Double.toString(averageGrade));


    }


    public void saveMeal(View view) {

        Meal meal = new Meal();

        meal.setHealthyScore(healthGrade);
        meal.setTasteScore(tasteGrade);
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
// PUT BACK      meal.setCategory(spinner.getSelectedItem().toString());

        Date dateTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
        meal.setDateTime(dateFormat.format(dateTime));
        meal.setLatitude(0);
        meal.setLongitude(0);
        meal.setImagePath("insert ImagePath");

        long id = db.insertMeal(meal);

        Toast.makeText(this, "Saved to "+meal.getCategory(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Health: "+healthGrade, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Taste: "+tasteGrade, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Name: "+name.getText().toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Date: "+dateFormat.format(dateTime), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Health: "+healthGrade, Toast.LENGTH_SHORT).show();
        db.close();

        //Gör ett intent som öppnar Meal List

    }

    /*  LÄGG TILL I MEAL LIST
    public void deleteMeal(View view) {
        String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        id = Long.valueOf(text);

        int rows = db.deleteMeal(id);

        if(rows > 0) {
            Toast.makeText(getApplicationContext(), "Deleted "+rows+" row(s)", Toast.LENGTH_SHORT).show();
        } else if (rows == 0) {
            Toast.makeText(getApplicationContext(), "No rows deleted", Toast.LENGTH_SHORT).show();
        }



    } */

    public void updateMeal(View view) {

        //VI FÅR IN ID PÅ ANNAT SÄTT
        //String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        //id = Long.valueOf(text);

        Meal meal = db.getMeal(id);
        meal.setHealthyScore(healthGrade);
        meal.setTasteScore(tasteGrade);
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
//PUTBACK **** meal.setCategory(spinner.getSelectedItem().toString());
        meal.setImagePath("insert ImagePath");

        // Man kan inte ändra ID eller location??

        int rowsAffected = db.updateMeal(meal);

        if (rowsAffected > 0) {
            Toast.makeText(getApplicationContext(), rowsAffected+" rows updated", Toast.LENGTH_SHORT).show();
        }

    }

}
