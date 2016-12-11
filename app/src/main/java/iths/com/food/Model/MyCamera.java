package iths.com.food.model;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by asakwarnmark on 2016-11-15.
 */

public class MyCamera {

    public static final int CAMERA_REQUEST_CODE = 1;
    private static final String TAG = "LOGTAG";
    private Uri photoFilePath;
    private Context context;
    private static int currentOrientation;

    public MyCamera(Context context) {
        this.context = context;
    }

    public Uri getPhotoFilePath() {
        return photoFilePath;
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null) {
            File photo = createFile();
            Log.d(TAG, "Is File photo null? " + (photo == null));
            photoFilePath = Uri.fromFile(photo);
            Log.d(TAG, "photoFilePath = " + photoFilePath.getPath());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath);
            ((Activity) context).startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private File createFile() {
        File dir = getDirectory();
        return new File(dir, "foodflash" + (new Date().getTime()) + ".jpg");
    }

    private File createThumbnailFile() {
        File dir = getDirectory();
        String thumbnailFileName = getThumbnailFilePath(photoFilePath.getPath());
        return new File(dir, thumbnailFileName);
    }

    private File getDirectory() {
        File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File ffDir = new File(photoDir, "FoodFlash");
        if(!ffDir.exists()) {
            if(!ffDir.mkdirs()) {
                Log.d(TAG, "Could not create directory" + ffDir.toString());
            }
        }
        return ffDir;
    }

    public void showImage(ImageView imageView, int thumbnailHeight, int thumbnailWidth) {
        int imageViewHeight = imageView.getHeight();
        int imageViewWidth = imageView.getWidth();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        int scaleFactor = 1;
        try {
            scaleFactor = Math.min(opt.outHeight / imageViewHeight, opt.outWidth / imageViewWidth);
        } catch(Exception e) {
            Log.d(TAG, "Could not scale image.");
        }

        Log.d(TAG, "scaleFactor: " + scaleFactor);
        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap rotatedBitmap = rotatePhoto(photoFilePath.getPath());

        Bitmap thumbnail = makeThumbnail(photoFilePath.getPath(), thumbnailHeight, thumbnailWidth);
        Log.d(TAG, "Is thumbnail null? " + (thumbnail == null));
        saveThumbnail(thumbnail, getThumbnailFilePath(photoFilePath.getPath()));

        imageView.setImageBitmap(rotatedBitmap);
    }

    public static Bitmap rotatePhoto(String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);

        ExifInterface exif;
        int orientation = 167;

        try {
            exif = new ExifInterface(filePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            currentOrientation = orientation;
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

        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }


    private Bitmap makeThumbnail(String largeImageFilePath, int height, int width) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(largeImageFilePath, opt);

        int scaleFactor = 1;
        try {
            scaleFactor = Math.min(opt.outHeight / height, opt.outWidth / width);
        } catch(Exception e) {
            Log.d(TAG, "Could not scale thumbnail.");
        }

        Log.d(TAG, "thumbnail scaleFactor: " + scaleFactor);
        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap thumbnail = BitmapFactory.decodeFile(largeImageFilePath, opt);
        Log.d(TAG, "Is thumbnail null in makeThumbnail? " + (thumbnail == null));
        return thumbnail;
    }

    private void saveThumbnail(Bitmap image, String thumbnailFilePath) {
        File file = new File(thumbnailFilePath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            Log.d(TAG, "Is FileOutputStream null? " + (out == null));
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Log.d(TAG, "Saving thumbnail succeeded");
        } catch (Exception e) {
            Log.d(TAG, "Saving thumbnail failed");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getThumbnailFilePath(String filePath) {
        String newFilePath = filePath.substring(0, filePath.length() - 4) + "m.jpg";
        Log.d(TAG, "thumbnail file path: " + newFilePath);
        return newFilePath;
    }


}
