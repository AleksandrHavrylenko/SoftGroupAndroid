package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.fragments.DialogFragment;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPass;
    private EditText editTextConfirmPass;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        buttonRegister.setOnClickListener(v -> onClickRegister());
    }

    private void findViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPass = (EditText) findViewById(R.id.editTextConfirmPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void onClickRegister() {
        String email = editTextEmail.getText().toString();
        String pass1 = editTextPass.getText().toString();
        String pass2 = editTextConfirmPass.getText().toString();

        SqLiteStorage dao = SqLiteStorage.getInstance();

        if (email.length() < 6) {
            String msg = getResources().getString(R.string.err_short_email);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            return;
        }
        if (!isValidEmailAddress(email)) {
            String msg = getResources().getString(R.string.not_valid_email);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            return;
        }
        if (dao.contains(email)) {
            String msg = getResources().getString(R.string.err_user_exists);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            return;
        }
        if (pass1.length() < 4) {
            String msg = getResources().getString(R.string.err_short_password);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            return;
        }
        if (!pass1.equals(pass2)) {
            String msg = getResources().getString(R.string.err_pass_not_equals);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            return;
        }

        dao.addUser(email, pass1);

        Toast.makeText(this, R.string.successful_registration, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean isValidEmailAddress(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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


