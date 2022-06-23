package com.hackathon.accesibility

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
	private var speechRecognizer: SpeechRecognizer? = null
	private var editText: EditText? = null
	private var textToSpeech: TextToSpeech? = null

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			checkPermission()
		}
		editText = findViewById(R.id.text)
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
		textToSpeech = TextToSpeech(this) { }
		textToSpeech!!.language = Locale.US
		speechRecognizer?.setRecognitionListener(object : RecognitionListener {
			override fun onReadyForSpeech(bundle: Bundle) {}
			override fun onBeginningOfSpeech() {
				editText?.setText("")
				editText?.setHint("Listening...")
			}

			override fun onRmsChanged(v: Float) {}
			override fun onBufferReceived(bytes: ByteArray) {}
			override fun onEndOfSpeech() {}
			override fun onError(i: Int) {
				textToSpeech!!.speak("Hey loser say it again", TextToSpeech.QUEUE_ADD, null)
			}

			override fun onResults(bundle: Bundle) {
				val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
				editText?.setText(data!![0])
				textToSpeech!!.speak("", TextToSpeech.QUEUE_ADD, null)
			}

			override fun onPartialResults(bundle: Bundle) {}
			override fun onEvent(i: Int, bundle: Bundle) {}
		})
	}

	override fun onDestroy() {
		super.onDestroy()
		speechRecognizer!!.destroy()
	}

	private fun checkPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RecordAudioRequestCode)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == RecordAudioRequestCode && grantResults.size > 0) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private var upKeyTime: Long = 0
	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (KeyEvent.KEYCODE_VOLUME_DOWN == event.keyCode) {
			if (event.eventTime - upKeyTime < PRESS_INTERVAL) {
				textToSpeech!!.speak("Please speak now", TextToSpeech.QUEUE_ADD, null)
				val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
				speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
				speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
				speechRecognizer!!.startListening(speechRecognizerIntent)
			}
			return true
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			return true
		}
		if (event.action == MotionEvent.ACTION_UP) {
			speechRecognizer!!.stopListening()
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
		if (KeyEvent.KEYCODE_VOLUME_UP == keyCode) {
			upKeyTime = event.eventTime
		}
		return super.onKeyUp(keyCode, event)
	}

	companion object {
		const val RecordAudioRequestCode = 1
		private const val PRESS_INTERVAL = 1000
	}
}