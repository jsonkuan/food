package iths.com.food.Helper.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Meal;

/**
 * Created by Hristijan on 2016-12-09.
 *
 * Checks if received sms contains this apps name. If so, it parses it to get meal id, healthy score and tasty score
 * Then it updates the database with those new values for the give meal.
 * A Toast is shown to the user then this is done.
 *
 * This class should not be instanced.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";

    private DatabaseHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DatabaseHelper(context);

        try {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        for (Object pdu : pdus) {

                            SmsMessage sms = getIncomingSms(pdu, bundle);
                            String senderNo = sms.getDisplayOriginatingAddress();
                            String msg = sms.getDisplayMessageBody();

                            //parse app name and return the meal id, healthy score and tasty score
                            Meal receivedMeal = parseReceivedSms(msg);

                            //update database
                            updateMeal(receivedMeal);

                            Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + msg, Toast.LENGTH_LONG).show();

                            Log.d(TAG, "onReceive: Sms received from number " + senderNo);
                        }
                        this.abortBroadcast();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onReceive: " +e);
        }
    }

    // Updates the given meal in the database
    private void updateMeal(Meal receivedMeal) {
        if(receivedMeal != null) {
            Meal meal = db.getMeal(receivedMeal.getId());
            meal.setTasteScore(receivedMeal.getTasteScore());
            meal.setHealthyScore(receivedMeal.getHealthyScore());
            db.updateMeal(meal);
        }
    }

    //receive sms syntax: *AppName* MealId: <number> Tasty: <number> Healthy: <number>
    private Meal parseReceivedSms(String msg) {
        Meal meal = null;
        msg = msg.toLowerCase();

        if(msg.startsWith("*foodflash*")){

            // Parse mealId
            int beginIdxMealId = msg.indexOf("mealid:")+ "mealid:".length();
            int endIdxMealId = msg.indexOf("tasty:");
            String strMealId = msg.substring(beginIdxMealId,endIdxMealId).trim();
            long mealId = Long.valueOf(strMealId);

            // Parse testy score
            int beginIdxTasty = msg.indexOf("tasty:")+ "tasty:".length();
            int endIdxTasty = msg.indexOf("healthy:");
            String strTasty = msg.substring(beginIdxTasty,endIdxTasty).trim();
            int tastyScore = Integer.valueOf(strTasty);

            // Parse Healthy score
            int beginIdxHealthy= msg.indexOf("healthy:")+ "healthy:".length();
            String strHealthy = msg.substring(beginIdxHealthy).trim();
            int healthyScore = Integer.valueOf(strHealthy);

            meal = new Meal();
            meal.setId(mealId);
            meal.setTasteScore(tastyScore);
            meal.setHealthyScore(healthyScore);
        }

        return meal;
    }


    private SmsMessage getIncomingSms(Object pdu, Bundle bundle) {
        SmsMessage sms;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            sms = SmsMessage.createFromPdu((byte[]) pdu, format);
        }
        else {
            sms = SmsMessage.createFromPdu((byte[]) pdu);
        }

        return sms;
    }
}