package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ua.sg.academy.havrulenko.android.HashUtils;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.CurrentStorage;
import ua.sg.academy.havrulenko.android.dao.UsersDaoInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonLogin;
    private AppCompatButton buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        Log.d(TAG, CurrentStorage.getCurrent().getAllRecordsLog());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                onClickLogin();
                break;
            case R.id.buttonRegister:
                onClickRegister();
                break;
        }
    }

    private void findViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
    }

    private void onClickLogin() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        String hash = HashUtils.sha512(password);
        Log.d(TAG, "onClickLogin: hash:" + hash);

        UsersDaoInterface dao = CurrentStorage.getCurrent();
        if(dao.contains(email)) {
            String pass = dao.getPasswordByEmail(email);
            if(pass.equals(hash)) {
                Intent intent = new Intent(MainActivity.this, SuccessLoginActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                String msg = getResources().getString(R.string.invalid_password);
                DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
            }
        } else {
            String msg = getResources().getString(R.string.invalid_email);
            DialogFragment.newInstance(msg).show(getSupportFragmentManager(), msg);
        }
    }

    private void onClickRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
