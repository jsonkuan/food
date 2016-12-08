package iths.com.food.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import iths.com.food.R;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by asakwarnmark on 2016-11-15.
 */

public class MyCamera {

    public static final int CAMERA_REQUEST_CODE = 1;
    private static final String TAG = "TAG";
    private Uri photoFilePath;
    Context context;

    public MyCamera(Context context) {
        this.context = context;
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null) {
            File photo = createFile();
            photoFilePath = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath);
            ((Activity) context).startActivityForResult(intent, CAMERA_REQUEST_CODE);

        }
    }

    private File createFile() {
        Log.d(TAG, "In createFile()");
        File dir = getDirectory();
        Log.d(TAG, "Got directory");
        File file = new File(dir, "iths" + (new Date().getTime()) + ".jpg");
        Log.d(TAG, "Created photo file: " + file.toString());
        return file;
    }

    private File getDirectory() {
        Log.d(TAG, "In getDirectory");
        File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File ithsDir = new File(photoDir, "ithsPics");
        if(!ithsDir.exists()) {
            if(!ithsDir.mkdirs()) {
                Log.d(TAG, "Could not create directory" + ithsDir.toString());
            }
        }
        return ithsDir;
    }

    public void showImage(ImageView imageView) {
        int imageViewHeight = imageView.getHeight();
        int imageViewWidth = imageView.getWidth();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        int scaleFactor = 1;

        try {
            scaleFactor = Math.min(opt.outHeight / imageViewHeight, opt.outWidth / imageViewWidth);
        } catch(Exception e) {
            Log.d(TAG, "Funkade inte att skala");
        }

        Log.d(TAG, "scaleFactor: " + scaleFactor);
        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap rotatedBitmap = rotateImage(photoFilePath.getPath());

        imageView.setImageBitmap(rotatedBitmap);
    }

    public static Bitmap rotateImage(String filePath) {
        Log.d(TAG, "About to decodeFile");
        Bitmap image = BitmapFactory.decodeFile(filePath);
        Log.d(TAG, "Is image null? " + (image == null));

        ExifInterface exif;
        int orientation = 167;

        try {
            exif = new ExifInterface(filePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("ORIENTATION", orientation + "");
        } catch (Exception e) {
            Log.d(TAG, "Error with photo file path");
        }

        Matrix matrix = new Matrix();
        if(orientation == 6) {
            matrix.postRotate(90);
        } else if(orientation == 8) {
            matrix.postRotate(270);
        } else if(orientation == 3) {
            matrix.postRotate(180);
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public void savePhoto() {

    }

    public void createThumbnail() {

    }

    public Uri getPhotoFilePath() {
        return photoFilePath;
    }

}
