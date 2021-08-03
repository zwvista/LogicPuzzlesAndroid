package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityHomeMainBinding
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import org.koin.android.ext.android.inject
import java.util.*

class HomeMainActivity : AppCompatActivity() {
    private val doc: HomeDocument by inject()
    private lateinit var binding: ActivityHomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        volumeControlStream = AudioManager.STREAM_MUSIC
        fun btnResumeGame() {
            resumeGame(doc.gameProgress().gameName, doc.gameProgress().gameTitle, true)
        }
        binding.btnResumeGame.setOnClickListener {
            btnResumeGame()
        }
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                btnResumeGame()
            }
        }
        binding.btnChooseGame.setOnClickListener {
            resultLauncher.launch(Intent(this, HomeChooseGameActivity::class.java))
        }
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HomeOptionsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnResumeGame.text = "Resume Game " + doc.gameProgress().gameTitle
    }

    private fun resumeGame(gameName: String, gameTitle: String, toResume: Boolean) {
        doc.resumeGame(gameName, gameTitle)
        val cls = Class.forName("com.zwstudio.logicpuzzlesandroid.puzzles.${gameName.toLowerCase(Locale.ROOT)}.${gameName}MainActivity")
        val intent = Intent(this, cls)
        intent.putExtra("toResume", toResume)
        startActivity(intent)
    }
}