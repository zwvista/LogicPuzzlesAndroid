package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HiddenCloudsMainActivity : GameMainActivity<HiddenCloudsGame, HiddenCloudsDocument, HiddenCloudsGameMove, HiddenCloudsGameState>() {
    private val document: HiddenCloudsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HiddenCloudsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HiddenCloudsGameActivity::class.java))
    }
}

class HiddenCloudsOptionsActivity : GameOptionsActivity<HiddenCloudsGame, HiddenCloudsDocument, HiddenCloudsGameMove, HiddenCloudsGameState>() {
    private val document: HiddenCloudsDocument by inject()
    override val doc get() = document
}

class HiddenCloudsHelpActivity : GameHelpActivity<HiddenCloudsGame, HiddenCloudsDocument, HiddenCloudsGameMove, HiddenCloudsGameState>() {
    private val document: HiddenCloudsDocument by inject()
    override val doc get() = document
}