package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var textAdvice: TextView
    lateinit var buttonAdvice: Button

    val adviceURL = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textAdvice = findViewById(R.id.textAdvice)
        buttonAdvice = findViewById(R.id.buttonAdvice)

        buttonAdvice.setOnClickListener {
            request()
        }

    }
    fun request(){
        CoroutineScope(Dispatchers.IO).launch {
            val data = async {
                randomAdvice()
            }.await()
            if (data.isNotEmpty())
            {
                updateAdvice(data)
            }
        }
    }
    fun randomAdvice():String{
        var response=""
        try {
            response = URL(adviceURL).readText(Charsets.UTF_8)

        }catch (e:Exception) {
            println("Error $e")
        }
        return response
    }
    suspend fun updateAdvice(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            textAdvice.text = advice
        }
    }
}