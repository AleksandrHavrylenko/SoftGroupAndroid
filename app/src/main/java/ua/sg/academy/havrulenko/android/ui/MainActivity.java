package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import ua.sg.academy.havrulenko.android.CurrentStorage;
import ua.sg.academy.havrulenko.android.HashUtils;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.UsersDaoInterface;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SESSION_EMAIL = "session_email";
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSession();
        setContentView(R.layout.activity_main);
        findViews();
        buttonLogin.setOnClickListener(v -> onClickLogin());
        buttonRegister.setOnClickListener(v -> onClickRegister());
        Log.d(TAG, CurrentStorage.getCurrent().getAllRecordsLog());
    }

    private void findViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void onClickLogin() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        String hash = HashUtils.sha512(password);
        Log.d(TAG, "onClickLogin: hash:" + hash);

        UsersDaoInterface dao = CurrentStorage.getCurrent();
        if (dao.contains(email)) {
            String pass = dao.getPasswordByEmail(email);
            if (pass.equals(hash)) {
                saveSession(email);
                Intent intent = new Intent(MainActivity.this, SuccessLoginActivity.class);
                intent.putExtra(KEY_SESSION_EMAIL, email);
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

    private void saveSession(String email) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_EMAIL, email);
        editor.apply();
    }

    private void loadSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data = sharedPreferences.getString(KEY_SESSION_EMAIL, "");
        if (!data.isEmpty()) {
            Intent intent = new Intent(this, SuccessLoginActivity.class);
            intent.putExtra(KEY_SESSION_EMAIL, data);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "Session not saved");
        }
    }

    private void onClickRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
