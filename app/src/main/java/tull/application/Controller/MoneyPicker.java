package tull.application.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import DataBaseLayer.UserData;

public class MoneyPicker extends DialogFragment {
    private NumberPicker numberPicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        LinearLayout linearLayout = new LinearLayout(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        numberPicker = new NumberPicker(getActivity());

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(500);
        numberPicker.setValue(100);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50,50);
        params.gravity = Gravity.CENTER;
        final LinearLayout.LayoutParams numPickerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        numPickerParams.weight = 1;
        linearLayout.addView(numberPicker,numPickerParams);
        builder.setTitle("Add Balance")
                .setPositiveButton("set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double newBalance = UnPaidTripsFragment.getUserBalance()+ numberPicker.getValue();
                        UserData.updateBalance(newBalance);
                       // PaymentFragment.balance.setText("$"+ newBalance);
                        // another one two
                    }
                });
        builder.setView(linearLayout);

        return builder.create();


    }
}
