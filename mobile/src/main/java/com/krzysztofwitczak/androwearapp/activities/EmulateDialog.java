package com.krzysztofwitczak.androwearapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.krzysztofwitczak.androwearapp.R;
import com.krzysztofwitczak.androwearapp.emotions.Emotion;
import com.krzysztofwitczak.androwearapp.emotions.EmotionClassifier;
import com.krzysztofwitczak.androwearapp.emotions.EmotionType;

public class EmulateDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.emulate_dialog, null);

        final Spinner spinner = (Spinner)v.findViewById(R.id.emotion_dropdown);
        ArrayAdapter<EmotionType> adapter = new ArrayAdapter<EmotionType>(getActivity(),
                android.R.layout.simple_spinner_item, EmotionType.values());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final NumberPicker np = (NumberPicker) v.findViewById(R.id.certainty);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " %";
            }
        });

        np.setMinValue(1);
        np.setMaxValue(100);
        np.setValue(50);

        builder.setView(v)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EmotionClassifier.emulateEmotion(
                                new Emotion((EmotionType) spinner.getSelectedItem(),
                                        np.getValue()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EmulateDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
