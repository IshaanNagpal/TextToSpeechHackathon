package com.hackathon.accesibility

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity

class PaymentSuccessActivity : AppCompatActivity() {

	private var textToSpeech: TextToSpeech? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.payment_success_activity_layout)
		textToSpeech = TextToSpeech(this) {
			textToSpeech!!.speak(
				"Your payment was succesfull",
				TextToSpeech.QUEUE_ADD,
				null
			)
		}
	}

}