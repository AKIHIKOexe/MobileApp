package com.example.mygame // Не забудьте поменять на свой package!

import androidx.lifecycle.ViewModel
import kotlin.random.Random
import kotlin.math.abs

class GameViewModel : ViewModel() {
    // Переменные состояния игры (приватные для изменения извне)
    var secretNumber: Int = 0
        private set
    var attempts: Int = 0
        private set
    var maxRange: Int = 0
        private set
    var isGameOver: Boolean = false
        private set

    // Инициализация новой игры с выбранным диапазоном
    fun startNewGame(range: Int) {
        maxRange = range
        secretNumber = Random.nextInt(1, range + 1)
        attempts = 0
        isGameOver = false
    }

    // Проверка введенного числа и генерация подсказки
    fun checkGuess(guess: Int): String {
        if (isGameOver) return "Игра окончена"

        attempts++
        val diff = abs(guess - secretNumber)

        return when {
            diff == 0 -> {
                isGameOver = true
                "🎉 Победа! Вы угадали за $attempts попыток!"
            }
            diff <= 3 -> "🔥 Горячо!"
            diff <= 10 -> "😐 Тепло"
            else -> "❄️ Холодно"
        }
    }

    // Расчет заполненности "термометра" для ProgressBar
    fun getThermometerProgress(guess: Int): Int {
        val diff = abs(guess - secretNumber)
        val maxDiff = maxRange.toFloat()
        // Чем ближе число, тем выше прогресс (от 0 до 100)
        val progress = ((maxDiff - diff) / maxDiff * 100).toInt()
        return progress.coerceIn(0, 100)
    }
}