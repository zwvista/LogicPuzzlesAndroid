package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LighthousesMainActivity : GameMainActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    private val document: LighthousesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LighthousesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LighthousesGameActivity::class.java))
    }
}

class LighthousesOptionsActivity : GameOptionsActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    private val document: LighthousesDocument by inject()
    override val doc get() = document
}

class LighthousesHelpActivity : GameHelpActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    private val document: LighthousesDocument by inject()
    override val doc get() = document
}