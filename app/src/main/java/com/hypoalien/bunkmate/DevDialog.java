package com.hypoalien.bunkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DevDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Developers")

                .setMessage("Hypoalien            (221710316002)\n" +
                        "B.Nagaraju           (221710316005)\n" +
                        "Guntha Sushanth      (221710316006)\n" +
                        "E.Eshwar             (221710316009)\n" +
                        "Raghava Reddy        (221710316010)\n" +
                        "Kodali Vishnu        (221710316015)\n" +
                        "P.Chenna Reddy\n"+
                        "Sai kiran Goud \n" +
                        "Aslaam\n" +
                        "K.Sathvik Reddy\n" +
                        "Peddi sai Teja reddy (221710316021)\n" +
                        "Aneesh Tumuluri      (221710316032)\n" +
                        "Audipudi Saketh      (221710316036)\n" +
                        "Satya Snehit         (221710316002)\n" +
                        "Harsha Varma         (221710314008)\n" +
                        "Siri Varma           (221710201012)")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
