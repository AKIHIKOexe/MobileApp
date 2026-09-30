package com.example.mygame // Не забудьте поменять на свой package!

import android.content.Context

class PrefsHelper(context: Context) {
    // Инициализируем локальное хранилище "GamePrefs"
    private val prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)

    // Сохраняем рекорд (количество попыток) для конкретного диапазона (50, 100, 200)
    fun saveRecord(range: Int, attempts: Int) {
        val currentRecord = getRecord(range)
        // Записываем только если это новый рекорд (или рекорда еще нет)
        if (currentRecord == 0 || attempts < currentRecord) {
            prefs.edit().putInt("record_$range", attempts).apply()
        }
    }

    // Получаем сохраненный рекорд. Если ничего нет — возвращаем 0
    fun getRecord(range: Int): Int {
        return prefs.getInt("record_$range", 0)
    }
}