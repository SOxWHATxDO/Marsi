package com.example.marsi

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class EmptyDispatcherActivity : AppCompatActivity() {

    private lateinit var btnChat: MaterialButton
    private lateinit var btnSchedule: MaterialButton
    private lateinit var chatLayout: View
    private lateinit var scheduleLayout: View
    private lateinit var spMon: Spinner
    private lateinit var spTue: Spinner
    private lateinit var spWed: Spinner
    private lateinit var spThu: Spinner
    private lateinit var spFri: Spinner
    private lateinit var spSat: Spinner
    private lateinit var spSun: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_dispatcher)

        btnChat = findViewById(R.id.btnChat)
        btnSchedule = findViewById(R.id.btnSchedule)
        chatLayout = findViewById(R.id.chatLayout)
        scheduleLayout = findViewById(R.id.scheduleLayout)

        spMon = findViewById(R.id.spMon)
        spTue = findViewById(R.id.spTue)
        spWed = findViewById(R.id.spWed)
        spThu = findViewById(R.id.spThu)
        spFri = findViewById(R.id.spFri)
        spSat = findViewById(R.id.spSat)
        spSun = findViewById(R.id.spSun)

        val couriers = arrayOf("Не назначен", "Иванов", "Петров", "Сидоров")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, couriers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        listOf(spMon, spTue, spWed, spThu, spFri, spSat, spSun).forEach { it.adapter = adapter }

        findViewById<Button>(R.id.btnSaveSchedule).setOnClickListener {
            val selected = listOf(
                "Пн" to spMon.selectedItem.toString(),
                "Вт" to spTue.selectedItem.toString(),
                "Ср" to spWed.selectedItem.toString(),
                "Чт" to spThu.selectedItem.toString(),
                "Пт" to spFri.selectedItem.toString(),
                "Сб" to spSat.selectedItem.toString(),
                "Вс" to spSun.selectedItem.toString()
            ).filter { it.second != "Не назначен" }

            val message = if (selected.isEmpty()) "Смены не назначены" else
                selected.joinToString("\n") { "${it.first}: ${it.second}" }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        btnChat.isCheckable = true
        btnSchedule.isCheckable = true

        btnChat.setOnClickListener {
            showChat()
        }
        btnSchedule.setOnClickListener {
            showSchedule()
        }
        showChat()
    }

    private fun showChat() {
        chatLayout.visibility = View.VISIBLE
        scheduleLayout.visibility = View.GONE
        btnChat.isChecked = true
        btnSchedule.isChecked = false
    }

    private fun showSchedule() {
        chatLayout.visibility = View.GONE
        scheduleLayout.visibility = View.VISIBLE
        btnChat.isChecked = false
        btnSchedule.isChecked = true
    }
}