package com.example.mygame // Не забудьте поменять на свой package!

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var prefsHelper: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsHelper = PrefsHelper(this)

        // Находим элементы интерфейса
        val btnStart = findViewById<MaterialButton>(R.id.btnStart)
        val rgDifficulty = findViewById<RadioGroup>(R.id.rgDifficulty)
        val tvRecord = findViewById<TextView>(R.id.tvRecord)

        // Обновляем текст рекорда при смене сложности
        rgDifficulty.setOnCheckedChangeListener { _, _ ->
            updateRecordText(tvRecord, rgDifficulty)
        }

        // Обработка нажатия кнопки "Начать игру"
        btnStart.setOnClickListener {
            // Определяем выбранный диапазон
            val range = when (rgDifficulty.checkedRadioButtonId) {
                R.id.rbEasy -> 50
                R.id.rbMedium -> 100
                R.id.rbHard -> 200
                else -> 50
            }

            // Передаем диапазон на второй экран через Intent
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("RANGE", range)
            startActivity(intent)
        }
    }

    // Обновляем рекорд при возвращении из игры
    override fun onResume() {
        super.onResume()
        val rgDifficulty = findViewById<RadioGroup>(R.id.rgDifficulty)
        val tvRecord = findViewById<TextView>(R.id.tvRecord)
        updateRecordText(tvRecord, rgDifficulty)
    }

    // Вспомогательный метод для отображения рекорда
    private fun updateRecordText(tvRecord: TextView, rgDifficulty: RadioGroup) {
        val range = when (rgDifficulty.checkedRadioButtonId) {
            R.id.rbEasy -> 50
            R.id.rbMedium -> 100
            R.id.rbHard -> 200
            else -> 50
        }
        val record = prefsHelper.getRecord(range)
        tvRecord.text = if (record == 0) "Рекорд: нет" else "Рекорд: $record попыток"
    }
}