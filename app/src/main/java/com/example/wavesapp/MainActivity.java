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
        // Этот макет может содержать просто логотип или кнопки для входа/регистрации
        setContentView(R.layout.activity_main);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btn_go_to_login);
        btnRegister = findViewById(R.id.btn_go_to_register);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistrationActivity.class)));

        // Пример реализации выхода из системы
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Вы вышли из аккаунта.", Toast.LENGTH_SHORT).show();
            // Обновить UI или скрыть кнопку выхода после выхода
            checkUserStateAndNavigate();
        });
    }

    // Этот метод жизненного цикла вызывается каждый раз при запуске/возвращении активности на передний план
    @Override
    public void onStart() {
        super.onStart();
        // Проверяем состояние пользователя
        checkUserStateAndNavigate();
    }

    private void checkUserStateAndNavigate() {
        // Получаем текущего пользователя. Если он не null, значит пользователь уже вошел в систему.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Пользователь АВТОРИЗОВАН
            Toast.makeText(this, "Пользователь уже вошел как: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();

            // ПЕРЕХОД на основной экран приложения (например, HomeActivity)
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            // finish(); // Можно закрыть MainActivity, чтобы пользователь не мог вернуться назад кнопкой "Назад"
        } else {
            // Пользователь НЕ АВТОРИЗОВАН
            // Остаемся на MainActivity (или переходим на LoginActivity, если MainActivity пустая)
            Toast.makeText(this, "Пользователь не авторизован. Остаемся на главном экране.", Toast.LENGTH_SHORT).show();

            // Если вы хотите, чтобы MainActivity была просто заставкой и сразу вела на логин,
            // то раскомментируйте код ниже:
            // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // startActivity(intent);
            // finish();
        }
    }
}