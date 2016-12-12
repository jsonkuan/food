package iths.com.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import iths.com.food.MainActivity;


public class WelcomeActivity extends AppCompatActivity {

    protected AlphaAnimation fadeIn;

    AnimatorSet mAnimationSet;
    ImageView myView;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        // set button invisible
        /*myView = (ImageView) findViewById(R.id.start);
        myView.setVisibility(View.INVISIBLE);*/

        //fade in
        TextView tvIntro = (TextView) findViewById(R.id.intro_text);
        fadeIn = new AlphaAnimation(0.0f , 1.0f );
        tvIntro.startAnimation(fadeIn);
        fadeIn.setDuration(3000);
        //fadeIn.setStartOffset(5000);


        /*
        // Intro sound
        final MediaPlayer mySound = MediaPlayer.create(this, R.raw.electrical);
        mySound.start();*/




        // Logo animation
        final ImageView img = (ImageView) findViewById(R.id.imageView);
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        // Logo fade out automatically
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
        img.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                // set button visible
                //myView.setVisibility(View.VISIBLE);

                //ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", .3f, 1f);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(img, "alpha", .3f, 1f);
                fadeIn.setDuration(500);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(img, "alpha",  1f, .3f);
                fadeOut.setDuration(500);

                mAnimationSet = new AnimatorSet();
                mAnimationSet.play(fadeIn).after(fadeOut);
                //mAnimationSet.play(fadeOut).after(fadeIn);
                mAnimationSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //mAnimationSet.start();
                    }
                });
                mAnimationSet.start();

                // When animation finish (or onClick), load Main Activity
                img.startAnimation(fadeOutAnimation);
                //finish();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void goToMainActivity(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}
