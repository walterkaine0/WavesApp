package com.example.wavesapp;
// В LoginActivity.java (импорты и инициализация аналогичны RegistrationActivity)
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Убедитесь, что у вас есть соответствующий XML-макет

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Вход успешен
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Вход успешен.",
                                        Toast.LENGTH_SHORT).show();
                                // TODO: Обновить UI или перейти к основному экрану
                            } else {
                                // Если вход не удался
                                Toast.makeText(LoginActivity.this, "Ошибка аутентификации.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Проверяем, вошел ли пользователь в систему (не null), и обновляем UI соответственно.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // TODO: Пользователь уже вошел в систему, можно перейти к основной активности
        }
    }
}

