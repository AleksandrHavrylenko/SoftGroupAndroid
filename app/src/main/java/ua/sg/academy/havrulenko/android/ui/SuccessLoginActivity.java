package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.fragments.AccountDetailsFragment;
import ua.sg.academy.havrulenko.android.model.Account;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ua.sg.academy.havrulenko.android.ui.AddPlaceActivity.KEY_EMAIL;
import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class SuccessLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        Button buttonUserList = (Button) findViewById(R.id.buttonUserList);
        Button buttonEditData = (Button) findViewById(R.id.buttonEditData);
        Button buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        Button buttonMyPlaces = (Button) findViewById(R.id.buttonMyPlaces);

        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        Account account = SqLiteStorage.getInstance().getUserByEmail(email);
        buttonLogout.setOnClickListener(v -> onClickLogout());
        buttonUserList.setVisibility(account.isAdmin() ? VISIBLE : GONE);
        buttonUserList.setClickable(account.isAdmin());
        buttonUserList.setOnClickListener(v -> onClickButtonUserList());
        buttonEditData.setOnClickListener(v -> onClickEditData(email));
        buttonAddPlace.setOnClickListener(v -> onClickAddPlace(email));
        buttonMyPlaces.setOnClickListener(v -> onClickMyPlaces(email));

        AccountDetailsFragment fragment = AccountDetailsFragment.newInstance(email, false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
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

    private void onClickEditData(String email) {
        Intent intent = new Intent(this, EditUserDataActivity.class);
        intent.putExtra(KEY_SESSION_EMAIL, email);
        startActivity(intent);
    }

    private void onClickAddPlace(String email) {
        Intent intent = new Intent(this, AddPlaceActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }

    private void onClickMyPlaces(String email) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }

    private void clearSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_EMAIL, "");
        editor.apply();
    }

}
