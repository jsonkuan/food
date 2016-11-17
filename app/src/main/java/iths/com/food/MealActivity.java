package iths.com.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.Date;

public class MealActivity extends AppCompatActivity {

    private boolean editable;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "TAG";
    private ImageView imageView;
    private static Uri photoFilePath;
    private static boolean isOpenedFromCameraActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_edit);
        if(isOpenedFromCameraActivity) {
            isOpenedFromCameraActivity = false;
            setContentView(R.layout.activity_meal_edit);
            setUpSpinner();
            imageView = (ImageView) findViewById(R.id.edit_meal_image);
            takePhoto(imageView);
        } else {
            setContentView(R.layout.activity_meal);
            imageView = (ImageView) findViewById(R.id.meal_image);
        }

    }

    public void makeEditable(View view) {
        setContentView(R.layout.activity_meal_edit);
        setUpSpinner();
    }

    public void saveChanges(View view) {
        //getStuffFromScreenAndMakeMealObject();
        //saveMealToDatabase();
        setContentView(R.layout.activity_meal);
        //setStuffOnScreenToNewMeal();
    }

    private void setUpSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_string_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    public static void setOpenedFromOtherActivity(boolean b) {
        isOpenedFromCameraActivity = b;
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
        int imageViewHeight = imageView.getHeight();
        int imageViewWidth = imageView.getWidth();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        int scaleFactor = 1;

        try {
            scaleFactor = Math.min(opt.outHeight / imageViewHeight, opt.outWidth / imageViewWidth);
        } catch(Exception e) {
            Log.d("Tag", "Funkade inte att skala");

        }

        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap image = BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        imageView.setImageBitmap(image);
    }



    //TODO: Reusable view with editable and non-editable objects
}
