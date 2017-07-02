package ua.sg.academy.havrulenko.android.fragments;


import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.FragmentAccountDetailsBinding;
import ua.sg.academy.havrulenko.android.model.Account;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class AccountDetailsFragment extends Fragment implements BanReasonFragment.BanReasonFragmentListener {
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ADMIN_MODE = "admin_mode";

    private String email;
    private boolean adminMode;
    private Account account;
    private FragmentAccountDetailsBinding binding;

    public static AccountDetailsFragment newInstance(String email, boolean adminMode) {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putBoolean(ARG_ADMIN_MODE, adminMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
            adminMode = getArguments().getBoolean(ARG_ADMIN_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        account = SqLiteStorage.getInstance().getUserByEmail(email);
        String fio = prepareText(account.getLastName()) + " " + prepareText(account.getFirstName()) + " " +
                prepareText(account.getMiddleName());
        binding.textViewFIO.setText(fio);
        binding.textViewEmail.setText(prepareText(email));
        binding.textViewNickname.setText(prepareText(account.getNickname()));
        binding.textViewPhone.setText(prepareText(account.getPhone()));

        if (account.getImage() != null) {
            Picasso.with(getContext()).load(new File(account.getImage())).into(binding.imageView);
        }

        binding.imageView.setOnClickListener(v ->
                PickImageDialog.build(new PickSetup().setWidth(512).setHeight(512))
                        .setOnPickResult(r -> {
                            binding.imageView.setImageBitmap(r.getBitmap());
                            Log.d("IMAGE", "path: " + r.getPath());
                            Log.d("URI", "uri: " + r.getUri());
                            account.setImage(r.getPath());
                            SqLiteStorage.getInstance().updateUser(account);
                        }).show(getActivity().getSupportFragmentManager()));

        if (adminMode && !account.isAdmin()) {
            binding.cardViewBan.setVisibility(VISIBLE);
            binding.textViewBan.setText(prepareBanText(account.getBannedTo()));
            binding.imageViewRemoveBan.setOnClickListener(v -> onClickRemoveBan());
            binding.imageViewNewBan.setOnClickListener(v -> onClickNewBan());
            binding.imageViewBanReason.setOnClickListener(v -> onClickBanReason());

        } else {
            binding.cardViewBan.setVisibility(GONE);
        }
    }

    private void onClickRemoveBan() {
        account.setBannedTo(0);
        account.setBanReason(null);
        SqLiteStorage.getInstance().updateUser(account);
        binding.textViewBan.setText(prepareBanText(0));
    }

    private void onClickNewBan() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog =
                new DatePickerDialog(binding.getRoot().getContext(), (view, year, month, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, month, dayOfMonth, 0, 0, 0);
                    if (newDate.getTimeInMillis() < System.currentTimeMillis()) {
                        Toast.makeText(binding.getRoot().getContext(), R.string.incorrect_date, Toast.LENGTH_LONG).show();
                    } else {
                        account.setBannedTo(newDate.getTimeInMillis());
                        SqLiteStorage.getInstance().updateUser(account);
                        binding.textViewBan.setText(prepareBanText(newDate.getTimeInMillis()));
                    }
                }, c.get(YEAR), c.get(MONTH), c.get(DAY_OF_MONTH));
        dialog.show();
    }

    private void onClickBanReason() {
        long date = account.getBannedTo();
        if (date == 0 && date < System.currentTimeMillis()) {
            return;
        }
        BanReasonFragment banReasonFragment = BanReasonFragment.newInstance(account.getBanReason());
        banReasonFragment.setTargetFragment(this, 0);
        banReasonFragment.show(getActivity().getSupportFragmentManager(), "ban_reason");
    }

    private String prepareText(String str) {
        return str == null ? "null" : str;
    }

    private String prepareBanText(long date) {
        if (date == 0 && date < System.currentTimeMillis()) {
            binding.imageViewBanReason.setVisibility(GONE);
            return getString(R.string.not_banned);
        }
        binding.imageViewBanReason.setVisibility(VISIBLE);
        String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date);
        String reason = account.getBanReason();
        return getString(R.string.ban_info_admin, formattedDate, reason);
    }


    @Override
    public void onFinishEditDialog(String inputText) {
        account.setBanReason(inputText);
        SqLiteStorage.getInstance().updateUser(account);
        binding.textViewBan.setText(prepareBanText(account.getBannedTo()));
    }
}
