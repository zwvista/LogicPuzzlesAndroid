package com.zwstudio.logicpuzzlesandroid.home.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.common.android.yesNoDialog
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityHomeOptionsBinding
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import org.koin.android.ext.android.inject

class HomeOptionsActivity : AppCompatActivity() {
    private val doc: HomeDocument by inject()
    private val soundManager: SoundManager by inject()
    private lateinit var binding: ActivityHomeOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rec = doc.gameProgress()
        binding.ctvPlayMusic.isChecked = rec.playMusic
        binding.ctvPlaySound.isChecked = rec.playSound

        binding.ctvPlayMusic.setOnClickListener {
            binding.ctvPlayMusic.isChecked = !rec.playMusic
            realm.beginTransaction()
            rec.playMusic = !rec.playMusic
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
            soundManager.playOrPauseMusic()
        }
        binding.ctvPlaySound.setOnClickListener {
            binding.ctvPlaySound.isChecked = !rec.playSound
            realm.beginTransaction()
            rec.playSound = !rec.playSound
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        binding.btnDone.setOnClickListener {
            finish()
        }
        binding.btnDefault.setOnClickListener {
            yesNoDialog("Do you really want to reset the options?") {
                realm.beginTransaction()
                rec.playMusic = true
                rec.playSound = true
                realm.insertOrUpdate(rec)
                realm.commitTransaction()
                binding.ctvPlayMusic.isChecked = rec.playMusic
                soundManager.playOrPauseMusic()
                binding.ctvPlaySound.isChecked = rec.playSound
            }
        }
    }
}