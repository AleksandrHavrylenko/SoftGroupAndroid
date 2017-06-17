package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import ua.sg.academy.havrulenko.android.R;

import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class SuccessLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewUserEmail);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        textViewEmail.setText(getResources().getString(R.string.welcome_user_email, email));
        buttonLogout.setOnClickListener(v -> onClickLogout());
    }

    private void onClickLogout() {
        clearSession();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void clearSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_EMAIL, "");
        editor.apply();
    }

}
