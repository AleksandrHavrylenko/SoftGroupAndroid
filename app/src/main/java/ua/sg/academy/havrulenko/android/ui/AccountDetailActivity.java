package ua.sg.academy.havrulenko.android.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.databinding.ActivityAccountDetailBinding;

import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class AccountDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAccountDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_account_detail);
        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, AccountDetailsFragment.newInstance(email))
                .commit();
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
