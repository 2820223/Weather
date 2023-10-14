package uz.itteacher.myweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.itteacher.myweatherapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.done.setOnClickListener {
            val city = binding.cityName.text
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("city", city.toString())
            startActivity(intent)
        }
    }
}