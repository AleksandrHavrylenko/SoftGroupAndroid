package ua.sg.academy.havrulenko.android.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import ua.sg.academy.havrulenko.android.R;

public class DialogFragment extends android.support.v4.app.DialogFragment {

    public static DialogFragment newInstance(String message) {
        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        final String title = getString(R.string.error);
        final Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_warning);
        icon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(icon)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.cancel());
        return builder.create();
    }
}