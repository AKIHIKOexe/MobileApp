package com.example.mygame // Не забудьте поменять на свой package!

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class GameActivity : AppCompatActivity() {

    // Подключаем ViewModel (сохраняет состояние при повороте)
    private val viewModel: GameViewModel by viewModels()
    private lateinit var prefsHelper: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        prefsHelper = PrefsHelper(this)

        // Получаем диапазон, переданный из меню
        val range = intent.getIntExtra("RANGE", 50)

        // Если игра только началась, инициализируем ViewModel
        if (viewModel.maxRange == 0) {
            viewModel.startNewGame(range)
        }

        // Находим все элементы на экране
        val tvRangeInfo = findViewById<TextView>(R.id.tvRangeInfo)
        val etGuess = findViewById<TextInputEditText>(R.id.etGuess)
        val btnCheck = findViewById<MaterialButton>(R.id.btnCheck)
        val tvEmoji = findViewById<TextView>(R.id.tvEmoji)
        val tvHint = findViewById<TextView>(R.id.tvHint)
        val pbThermometer = findViewById<ProgressBar>(R.id.pbThermometer)
        val tvAttempts = findViewById<TextView>(R.id.tvAttempts)
        val btnSurrender = findViewById<MaterialButton>(R.id.btnSurrender)

        // Настраиваем начальный вид
        tvRangeInfo.text = "Введите число от 1 до ${viewModel.maxRange}"
        tvAttempts.text = "Попыток: ${viewModel.attempts}"

        // Кнопка "Проверить"
        btnCheck.setOnClickListener {
            val input = etGuess.text.toString()

            // Простая валидация ввода
            if (input.isEmpty()) {
                Toast.makeText(this, "Введите число!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val guess = input.toIntOrNull()
            if (guess == null || guess < 1 || guess > viewModel.maxRange) {
                Toast.makeText(this, "Число должно быть от 1 до ${viewModel.maxRange}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Получаем подсказку из логики ViewModel
            val hint = viewModel.checkGuess(guess)

            // Обновляем интерфейс
            tvHint.text = hint
            tvAttempts.text = "Попыток: ${viewModel.attempts}"
            etGuess.text?.clear()

            // Обновляем термометр
            val progress = viewModel.getThermometerProgress(guess)
            pbThermometer.progress = progress

            // Меняем смайлик и цвет в зависимости от результата
            when {
                hint.contains("Победа") -> {
                    tvEmoji.text = "😎"
                    pbThermometer.progress = 100
                    // Сохраняем рекорд в SharedPreferences
                    prefsHelper.saveRecord(viewModel.maxRange, viewModel.attempts)
                    Toast.makeText(this, "Рекорд сохранен!", Toast.LENGTH_SHORT).show()
                    btnCheck.isEnabled = false
                }
                hint.contains("Горячо") -> {
                    tvEmoji.text = "🔥"
                    // Красный цвет
                    pbThermometer.progressTintList = ColorStateList.valueOf(0xFFF44336.toInt())
                }
                hint.contains("Тепло") -> {
                    tvEmoji.text = "😐"
                    // Желтый цвет
                    pbThermometer.progressTintList = ColorStateList.valueOf(0xFFFFC107.toInt())
                }
                else -> {
                    tvEmoji.text = "❄️"
                    // Синий цвет
                    pbThermometer.progressTintList = ColorStateList.valueOf(0xFF2196F3.toInt())
                }
            }
        }

        // Кнопка "Сдаться" — показывает ответ и закрывает экран
        btnSurrender.setOnClickListener {
            Toast.makeText(this, "Загаданное число: ${viewModel.secretNumber}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}