package iths.com.food.Helper.sms;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import iths.com.food.Model.Meal;
import iths.com.food.R;

/**
 * Created by Hristijan on 2016-12-09.
 *
 * Creates a popup window so user can enter the phone number of the recipient.
 * Then it sends a sms containing: the apps name, the meals name, the meals id.
 * A toast is shown to the user that the message was send.
 */

public class SmsSender {

    // Creates a popup window for entering the phone number and sends the sms
    // Send sms syntax: <appName> <mealName> <mealId>
    public static void sendSms(final Context context, final Meal meal) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter recipients phone number, inc country code");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected.
        input.setInputType(InputType.TYPE_CLASS_PHONE);

        builder.setView(input);

        // Set up the buttons
        // Button OK
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String phoneNumber = input.getText().toString();
                String appName = context.getResources().getString(R.string.app_name);
                String msg = "*"+appName +"* Meal: "+ meal.getName() +" MealID: "+ meal.getId();

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, msg, null, null);

                Toast.makeText(context, "sms is sent", Toast.LENGTH_SHORT).show();
            }
        });
        // Button Cancel
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
