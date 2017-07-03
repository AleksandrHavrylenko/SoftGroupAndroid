package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.File;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.Utils;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.fragments.DialogFragment;

public class RegisterActivity extends AppCompatActivity {
    private static final String KEY_IMAGE_PATH = "image_path";

    private EditText editTextEmail;
    private EditText editTextPass;
    private EditText editTextConfirmPass;
    private Button buttonRegister;
    private ImageView imageViewUser;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState != null) {
            imagePath = savedInstanceState.getString(KEY_IMAGE_PATH);
            if (imagePath != null) {
                File imgFile = new File(String.valueOf(imagePath));
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageViewUser.setImageBitmap(myBitmap);
                }
            }
        }

        buttonRegister.setOnClickListener(v -> onClickRegister());
        imageViewUser.setOnClickListener(v ->
                PickImageDialog.build(new PickSetup().setWidth(512).setHeight(512))
                .setOnPickResult(r -> {
                    imageViewUser.setImageBitmap(r.getBitmap());
                    Log.d("IMAGE", "path: " + r.getPath());
                    Log.d("URI", "uri: " + r.getUri());
                    imagePath = Utils.saveUserImage(r.getBitmap());
                }).show(getSupportFragmentManager()));
    }

    private void findViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPass = (EditText) findViewById(R.id.editTextConfirmPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
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

        dao.addUser(email, pass1, imagePath);

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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_IMAGE_PATH, imagePath);
        super.onSaveInstanceState(outState);
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


