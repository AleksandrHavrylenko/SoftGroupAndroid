package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import ua.sg.academy.havrulenko.android.R;

public class SuccessLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewUserEmail);
        AppCompatButton buttonLogout = (AppCompatButton) findViewById(R.id.buttonLogout);
        String email = getIntent().getStringExtra("email");
        textViewEmail.setText(getResources().getString(R.string.welcome_user_email, email));
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
            }
        });
    }

    private void onClickLogout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
