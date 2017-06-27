package ua.sg.academy.havrulenko.android.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.FragmentAccountDetailsBinding;
import ua.sg.academy.havrulenko.android.sqlite.Account;

public class AccountDetailsFragment extends Fragment {
    private static final String ARG_EMAIL = "param1";

    private String email;
    private Account account;
    private FragmentAccountDetailsBinding binding;


    public AccountDetailsFragment() {

    }

    public static AccountDetailsFragment newInstance(String email) {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_account_details, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false);
        //AccountDetailsFragment binding = MainFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding


        return binding.getRoot();
    }

    private String prepareText(String str) {
        return str == null ? "null" : str;
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
    }
}
