package com.cloremdbdemo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteDialog extends DialogFragment {
    private DeleteListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String timestamp = getArguments().getString("timestamp");
        View view = inflater.inflate(R.layout.delete_dialog, container, false);

        view.findViewById(R.id.update_demo_text_button).setOnClickListener(l -> {
            String demoText = getArguments().getString("demoText");
            getDialog().dismiss();
            listener.onUpdateClicked(timestamp, demoText);

        });
        view.findViewById(R.id.cancel_delete_button).setOnClickListener(l -> {
            getDialog().dismiss();
        });
        view.findViewById(R.id.perform_delete_button).setOnClickListener(l -> {
            getDialog().dismiss();
            listener.onDeleteClicked(timestamp);
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Using this method to avoid dialog width getting trimmed


    }

    public interface DeleteListener {
        void onDeleteClicked(String timestamp);//We will trigger this method when user clicks on 'yes' button for deleting demo object

        void onUpdateClicked(String timestamp, String demoText);// We will trigger this method when user clicks on 'update' button for demo object
    }

    public void setListener(DeleteListener listener) {
        this.listener = listener;
    }
}
