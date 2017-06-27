package ua.sg.academy.havrulenko.android.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.ActivityEditUserDataBinding;
import ua.sg.academy.havrulenko.android.sqlite.Account;

import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class EditUserDataActivity extends AppCompatActivity {

    private ActivityEditUserDataBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_user_data);
        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        Account account = SqLiteStorage.getInstance().getUserByEmail(email);
        mBinding.editTextFirstName.setText(account.getFirstName());
        mBinding.editTextLastName.setText(account.getLastName());
        mBinding.editTextMiddleName.setText(account.getMiddleName());
        mBinding.editTextNickname.setText(account.getNickname());
        mBinding.editTextPhoneNumber.setText(account.getPhone());

        mBinding.buttonSave.setOnClickListener(v -> {
            account.setFirstName(mBinding.editTextFirstName.getText().toString());
            account.setLastName(mBinding.editTextLastName.getText().toString());
            account.setMiddleName(mBinding.editTextMiddleName.getText().toString());
            account.setNickname(mBinding.editTextNickname.getText().toString());
            account.setPhone(mBinding.editTextPhoneNumber.getText().toString());
            SqLiteStorage.getInstance().updateUser(account);
            finish();
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
