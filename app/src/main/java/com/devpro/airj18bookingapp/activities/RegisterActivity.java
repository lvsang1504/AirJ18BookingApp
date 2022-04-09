package com.devpro.airj18bookingapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.devpro.airj18bookingapp.R;
import com.devpro.airj18bookingapp.listeners.RegisterResponseListener;
import com.devpro.airj18bookingapp.models.UserRegister;
import com.devpro.airj18bookingapp.models.UserResponse;
import com.devpro.airj18bookingapp.repository.RequestManager;
import com.devpro.airj18bookingapp.utils.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private String encodedImage;
    final Calendar myCalendar = Calendar.getInstance();
    int itemSelected = 0;
    String[] items = new String[]{"Select Sex", "Male", "Female", "Other"};

    TextView textSignIn, textAddImage, textView;
    Button buttonSignUp;
    FrameLayout layoutImage;
    ProgressBar progressBar;
    RoundedImageView imageProfile;
    Spinner dropdown;
    Animation anim_from_button, anim_from_top, anim_from_left;
    RequestManager requestManager;

    EditText inputFirstName, inputLastName, inputBirthday, inputPhone, inputEmail, inputPassword, inputConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textSignIn = findViewById(R.id.textSignIn);
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputBirthday = findViewById(R.id.inputBirthday);
        inputPhone = findViewById(R.id.inputPhone);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        layoutImage = findViewById(R.id.layoutImage);
        progressBar = findViewById(R.id.progressBar);
        imageProfile = findViewById(R.id.imageProfile);
        textAddImage = findViewById(R.id.textAddImage);
        textView = findViewById(R.id.textView);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        inputBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dropdown = findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSelected = 0;
            }
        });

        preferenceManager = new PreferenceManager(getApplicationContext());
        requestManager = new RequestManager(this);
        setListeners();

        //Load Animations
        anim_from_button = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        anim_from_top = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        anim_from_left = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);

        textView.setAnimation(anim_from_top);
        layoutImage.setAnimation(anim_from_top);
        inputEmail.setAnimation(anim_from_left);
        inputFirstName.setAnimation(anim_from_left);
        inputLastName.setAnimation(anim_from_left);
        inputBirthday.setAnimation(anim_from_left);
        dropdown.setAnimation(anim_from_left);
        inputPassword.setAnimation(anim_from_left);
        inputPhone.setAnimation(anim_from_left);
        inputConfirmPassword.setAnimation(anim_from_left);
        dropdown.setAnimation(anim_from_left);
        buttonSignUp.setAnimation(anim_from_button);
        textSignIn.setAnimation(anim_from_button);


        //Hide status bar and navigation bar at the bottom
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        this.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        );
    }

    private void setListeners() {
        textSignIn.setOnClickListener(v -> onBackPressed());
        buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });

        layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void signUp() {
        loading(true);
        System.out.println(items[itemSelected].toUpperCase());
        if (isValidSignUpDetails()) {
            requestManager.getRegister(registerResponseListener, new UserRegister(
                    inputFirstName.getText().toString().trim(),
                    inputLastName.getText().toString().trim(),
                    inputPhone.getText().toString().trim(),
                    inputEmail.getText().toString().trim(),
                    inputPassword.getText().toString().trim(),
                    items[itemSelected].toUpperCase(),
                    inputBirthday.getText().toString()
            ));
        }

//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> user = new HashMap<>();
//        user.put(Constants.KEY_NAME, inputName.getText().toString());
//        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
//        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());
//        user.put(Constants.KEY_IMAGE, encodedImage);
//        database.collection(Constants.KEY_COLLECTION_USER)
//                .add(user)
//                .addOnSuccessListener(documentReference -> {
//                    loading(false);
//                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
//                    preferenceManager.putString(Constants.KEY_NAME, inputName.getText().toString());
//                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                })
//                .addOnFailureListener(e -> {
//                    loading(false);
//                    showToast(e.getMessage());
//                });
    }

    private RegisterResponseListener registerResponseListener = new RegisterResponseListener() {
        @Override
        public void didFetch(UserResponse userResponse, String message) {
            if (userResponse.error == null) {
                Toast.makeText(RegisterActivity.this, "Register success! Congratulation!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(RegisterActivity.this,"Error1: "+ userResponse.error.toString(), Toast.LENGTH_SHORT).show();
                loading(false);
            }
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RegisterActivity.this,"Error2: "+ message, Toast.LENGTH_SHORT).show();
            loading(false);
        }
    };

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageProfile.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        inputBirthday.setText(dateFormat.format(myCalendar.getTime()));
    }

    private Boolean isValidSignUpDetails() {
//        if (encodedImage == null) {
//            showToast("Select profile image");
//            return false;
//        } else
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            showToast("Enter first name");
            return false;
        } else if (inputLastName.getText().toString().trim().isEmpty()) {
            showToast("Enter last name");
            return false;
        } else if (inputBirthday.getText().toString().trim().isEmpty()) {
            showToast("Enter birthday");
            return false;
        } else if (itemSelected == 0) {
            showToast("Select sex");
            return false;
        } else if (inputPhone.getText().toString().trim().isEmpty()) {
            showToast("Enter phone number");
            return false;
        } else if (inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())) {
            showToast("Password & confirm password must be same");
            return false;
        } else return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            buttonSignUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            buttonSignUp.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}