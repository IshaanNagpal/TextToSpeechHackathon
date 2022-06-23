package com.hackathon.accesibility

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class CardActivity : AppCompatActivity() {
	@SuppressLint("ClickableViewAccessibility")
	private var textToSpeech: TextToSpeech? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_card)
		textToSpeech = TextToSpeech(this) {
			textToSpeech!!.speak(
				"I have opened the card payment. YOu have an outstanding balance of 3245 dollars. Shall I proceed with the payment?",
				TextToSpeech.QUEUE_ADD,
				null
			)
		}
		textToSpeech!!.language = Locale.ENGLISH
	}

	override fun onResume() {
		super.onResume()

		Handler().postDelayed({
			startActivity(Intent(this, PaymentSuccessActivity::class.java))
			finish()
		}, 15000)
	}

}
