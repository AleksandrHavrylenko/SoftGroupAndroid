package ua.sg.academy.havrulenko.android.fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import ua.sg.academy.havrulenko.android.R;

public class BanReasonFragment extends android.support.v4.app.DialogFragment {

    private static final String ARG_REASON = "reason";
    private BanReasonFragmentListener listener;

    public static BanReasonFragment newInstance(String message) {
        BanReasonFragment dialogFragment = new BanReasonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REASON, message);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (BanReasonFragmentListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement BanReasonFragmentListener interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getString(R.string.ban_reason);
        final String oldReason = getArguments().getString(ARG_REASON, "");
        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(oldReason);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setCancelable(false)
                .setView(input)
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel())
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    String reason = input.getText().toString();
                    listener.onFinishEditDialog(reason);
                    dialog.dismiss();
                });
        return builder.create();
    }

    interface BanReasonFragmentListener {
        void onFinishEditDialog(String inputText);
    }
}