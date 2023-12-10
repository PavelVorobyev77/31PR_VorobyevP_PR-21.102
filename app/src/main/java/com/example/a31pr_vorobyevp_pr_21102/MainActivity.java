package com.example.a31pr_vorobyevp_pr_21102;

import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewFlipper flipper;

    AnimationSet animSetFlipInForward;
    AnimationSet animSetFlipOutForward;
    AnimationSet animSetFlipInBackward;
    AnimationSet animSetFlipOutBackward;

    Button btnShow;
    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "111",
            CHANNEL_NAME = "For Duke";
    private ImageButton cameraButton;
    private ImageButton mapsButton;
    private ImageButton googleButton;
    private ImageButton contactsButton;
    private ImageButton phoneButton;
    private ImageButton otherAppButton;
    RadioButton showButtonRadio;
    RadioButton hideButtonRadio;
    LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showButtonRadio = findViewById(R.id.showButtonRadio);
        hideButtonRadio = findViewById(R.id.hideButtonRadio);
        buttonsLayout = findViewById(R.id.buttonsLayout);

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        flipper = findViewById(R.id.viewflipper);

        animSetFlipInForward = new AnimationSet(true);
        TranslateAnimation translateAnimFlipInForward = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animSetFlipInForward.addAnimation(translateAnimFlipInForward);
        animSetFlipInForward.setDuration(500);
        animSetFlipInForward.setInterpolator(new OvershootInterpolator());

        animSetFlipOutForward = new AnimationSet(true);
        TranslateAnimation translateAnimFlipOutForward = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animSetFlipOutForward.addAnimation(translateAnimFlipOutForward);
        animSetFlipOutForward.setDuration(500);
        animSetFlipOutForward.setInterpolator(new OvershootInterpolator());

        animSetFlipInBackward = new AnimationSet(true);
        TranslateAnimation translateAnimFlipInBackward = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animSetFlipInBackward.addAnimation(translateAnimFlipInBackward);
        animSetFlipInBackward.setDuration(500);
        animSetFlipInBackward.setInterpolator(new OvershootInterpolator());

        animSetFlipOutBackward = new AnimationSet(true);
        TranslateAnimation translateAnimFlipOutBackward = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animSetFlipOutBackward.addAnimation(translateAnimFlipOutBackward);
        animSetFlipOutBackward.setDuration(500);
        animSetFlipOutBackward.setInterpolator(new OvershootInterpolator());

        btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(v -> {
            showNotification();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel catChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            catChannel.enableVibration(true);
            catChannel.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(catChannel);
            }
        }

        showButtonRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonsLayout.setVisibility(View.VISIBLE);
                    hideButtonRadio.setChecked(false);
                }
            }
        });

        hideButtonRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonsLayout.setVisibility(View.GONE);
                    showButtonRadio.setChecked(false);
                }
            }
        });
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(this);

        mapsButton = findViewById(R.id.mapsButton);
        mapsButton.setOnClickListener(this);

        googleButton = findViewById(R.id.googleButton);
        googleButton.setOnClickListener(this);

        phoneButton = findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(this);

        contactsButton = findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(this);

        otherAppButton = findViewById(R.id.otherAppButton);
        otherAppButton.setOnClickListener(this);
    }

    private void SwipeLeft() {
        flipper.setInAnimation(animSetFlipInBackward);
        flipper.setOutAnimation(animSetFlipOutBackward);
        flipper.showPrevious();

    }

    private void SwipeRight() {
        flipper.setInAnimation(animSetFlipInForward);
        flipper.setOutAnimation(animSetFlipOutForward);
        flipper.showNext();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float sensitivity = 50;
            if ((e1.getX() - e2.getX()) > sensitivity) {
                SwipeLeft();
            } else if ((e2.getX() - e1.getX()) > sensitivity) {
                SwipeRight();
            }

            return true;
        }
    };

    GestureDetector gestureDetector = new GestureDetector(getBaseContext(), simpleOnGestureListener);

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cameraButton) {
            openCamera();
        } else if (v.getId() == R.id.mapsButton) {
            openMaps();
        } else if (v.getId() == R.id.googleButton) {
            openGoogle();
        } else if (v.getId() == R.id.contactsButton) {
            openContacts();
        } else if (v.getId() == R.id.phoneButton) {
            openPhone();
        } else if (v.getId() == R.id.otherAppButton) {
            openOtherApp();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void openMaps() {
        Uri locationUri = Uri.parse("geo:0,0?q=Новосибирский авиационно-технический колледж имени Б.С.Галущака");
        Intent intent = new Intent(Intent.ACTION_VIEW, locationUri);
        startActivity(intent);
    }

    private void openGoogle() {
        Uri googleUri = Uri.parse("http://www.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, googleUri);
        startActivity(intent);
    }

    private void openContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

    private void openPhone() {
        String phoneNumber = "tel:666";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }

    private void openOtherApp() {
        String url = "https://natk.ru/stud-grad/schedule/187?group=%D0%9F%D0%A0-21.102";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    public void showNotification() {
        Intent notificationIntent = new Intent(MainActivity.this, Notification.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Указываем звук для уведомления из ресурсов
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.duke);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.duke)
                .setContentTitle("Напоминание")
                .setContentText("Пора истреблять пришельцев!!")
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.duke))
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.duke))  // Указываем звук для уведомления
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }
}