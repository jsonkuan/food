package iths.com.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Meal;

public class ShareOnFacebookActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginManager manager;
    private DatabaseHelper db;
    private Meal meal;
    private long current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_facebook);

        Intent intent = getIntent();
        current_id = intent.getLongExtra("id",0);

        this.shareOnFacebook();

    }

    private void publishImage(){

        db = new DatabaseHelper(this);
        meal = db.getMeal(current_id);

        Bitmap image = BitmapFactory.decodeFile(meal.getImagePath());

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(meal.getName()+" - "+meal.getDescription())
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void shareOnFacebook() {

        Log.d("test","Share on facebook!");

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();
        manager.logInWithPublishPermissions(this, permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                publishImage();
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", error.getCause().toString());
            }
        });

    }

}
