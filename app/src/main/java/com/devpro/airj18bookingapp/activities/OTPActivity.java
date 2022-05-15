package com.devpro.airj18bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.devpro.airj18bookingapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OTPActivity extends AppCompatActivity {
    EditText editTextConfirmOTP;
    Button buttonConfirm;
    public String randomOTP="abc";
    String gmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        gmail=getIntent().getStringExtra("gmail");
        Log.d("gmail",gmail);
        randomOTP=createOTP();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sendOTP();
        setControl();
        setEvent();
    }
    private void sendOTP() {
        final String username="nhanhuu2808@gmail.com";
        final String password="Nhan2808";
        String messageToSend="Mã OTP Đặt phòng:"+ randomOTP;
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session=Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username,password);
                    }
                });
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(gmail));
            message.setSubject("Dat phong send OTP");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(this,"Send Email successfully",Toast.LENGTH_LONG).show();
        }catch (MessagingException e){
            throw  new RuntimeException(e);
        }

    }

    private void setEvent() {
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextConfirmOTP.getText().toString().equals(randomOTP)){
                    Intent intent = new Intent(OTPActivity.this, NewPasswordActivity.class);
                    intent.putExtra("gmail",gmail);
                    startActivity(intent);

                }
            }
        });
    }

    private void setControl() {
        editTextConfirmOTP=findViewById(R.id.editTextConfirmOTP);
        buttonConfirm=findViewById(R.id.buttonConfirm);
    }

    public String createOTP(){
        ArrayList<String> around = new ArrayList<>();
        String[] arrCode = new String[5];
        for(int i= 0; i<=9; i++){
            Integer tam = new Integer(i);
            around.add(tam.toString());
        }
        Random rand = new Random();
        randomOTP = "";
        for(int i =0; i< 5; i++){
            int randomInt = rand.nextInt(9);
            arrCode[i] = around.get(randomInt);
            System.out.println(arrCode[i]);
            randomOTP+=arrCode[i];
        }
        return randomOTP;
    }

}