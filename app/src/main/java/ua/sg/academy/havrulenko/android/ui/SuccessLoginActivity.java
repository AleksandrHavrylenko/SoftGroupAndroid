package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.sqlite.Account;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class SuccessLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewUserEmail);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        Button buttonUserList = (Button) findViewById(R.id.buttonUserList);
        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        Account account = SqLiteStorage.getInstance().getUserByEmail(email);
        textViewEmail.setText(getResources().getString(R.string.welcome_user_email, email));
        buttonLogout.setOnClickListener(v -> onClickLogout());
        buttonUserList.setVisibility(account.isAdmin() ? VISIBLE : GONE);
        buttonUserList.setClickable(account.isAdmin());
        buttonUserList.setOnClickListener(v -> onClickButtonUserList());
    }

    private void onClickLogout() {
        clearSession();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void onClickButtonUserList() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    private void clearSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_EMAIL, "");
        editor.apply();
    }

}
