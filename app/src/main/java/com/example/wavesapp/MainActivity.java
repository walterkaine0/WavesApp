package com.example.wavesapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnLogin, btnRegister, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btn_go_to_login);
        btnRegister = findViewById(R.id.btn_go_to_register);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistrationActivity.class)));


        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Вы вышли из аккаунта.", Toast.LENGTH_SHORT).show();
            checkUserStateAndNavigate();
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        checkUserStateAndNavigate();
    }

    private void checkUserStateAndNavigate() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            Toast.makeText(this, "Пользователь уже вошел как: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);

        } else {

            Toast.makeText(this, "Пользователь не авторизован. Остаемся на главном экране.", Toast.LENGTH_SHORT).show();
        }
    }
}