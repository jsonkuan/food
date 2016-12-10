package iths.com.food.helper.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import iths.com.food.helper.DatabaseHelper;
import iths.com.food.model.Meal;

/**
 * Created by Hristijan on 2016-12-09.
 *
 * Checks if received sms contains this apps name. If so, it parses it to get meal id, healthy score and tasty score
 * Then it updates the meal in the database with those new values.
 * A Toast is shown to the user then this is done.
 *
 * NOTES: This class should not be instanced.
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
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (Object pdu : pdus) {
                    SmsMessage sms = getIncomingSms(pdu, bundle);
                    String senderNo = sms.getDisplayOriginatingAddress();
                    String msg = sms.getDisplayMessageBody();

                    //parse received sms to get meal id, healthy score and tasty score
                    Meal receivedMeal = parseReceivedSms(msg);

                    //update meal in database an notify user
                    if(receivedMeal != null){
                        Meal meal = updateMeal(receivedMeal);
                        makeToast(context, meal.getName());

                        Log.i(TAG, "onReceive:\n MealID " + meal.getId() +
                                    "\n TastyScore " + meal.getTasteScore() +
                                    "\n HealthyScore " + meal.getHealthyScore());
                    }
                    Log.d(TAG, "onReceive: sender number: " + senderNo + " message: " + msg);
                }
                this.abortBroadcast();
            }
        } catch (Exception e) {
            Log.e(TAG, "onReceive: " + e.getMessage());
        }
    }

    // Updates the given meal in the database
    // Returns the updated meal
    private Meal updateMeal(Meal receivedMeal) {
        Meal meal = db.getMeal(receivedMeal.getId());
        meal.setTasteScore(receivedMeal.getTasteScore());
        meal.setHealthyScore(receivedMeal.getHealthyScore());
        db.updateMeal(meal);

        return meal;
    }

    //receive sms syntax: *AppName* MealId: <number> Tasty: <number> Healthy: <number>
    // Returns a meal that only contain mealId, testy score and healthy score
    private Meal parseReceivedSms(String msg) {
        Meal meal = null;
        msg = msg.toLowerCase();

        if(msg.contains("*foodflash*")){
            try {
                // Parse mealId
                int beginIdxMealId = msg.indexOf("mealid:") + "mealid:".length();
                int endIdxMealId = msg.indexOf("tasty:");
                String strMealId = msg.substring(beginIdxMealId, endIdxMealId).trim();
                long mealId = Long.valueOf(strMealId);

                // Parse testy score
                int beginIdxTasty = msg.indexOf("tasty:") + "tasty:".length();
                int endIdxTasty = msg.indexOf("healthy:");
                String strTastyScore = msg.substring(beginIdxTasty, endIdxTasty).trim();
                int tastyScore = Integer.valueOf(strTastyScore);

                // Parse Healthy score
                int beginIdxHealthy = msg.indexOf("healthy:") + "healthy:".length();
                String strHealthyScore = msg.substring(beginIdxHealthy).trim();
                int healthyScore = Integer.valueOf(strHealthyScore);

                // Create meal
                meal = new Meal();
                meal.setId(mealId);
                meal.setTasteScore(tastyScore);
                meal.setHealthyScore(healthyScore);
            }
            catch (Exception e){
                Log.e(TAG, "parseReceivedSms: " + e.getMessage());
            }
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

    private void makeToast(Context context, String mealName){
        Toast.makeText(context, "TastyScore and HealthyScore for Meal " +
                mealName + " was updated from sms", Toast.LENGTH_LONG).show();
    }
}