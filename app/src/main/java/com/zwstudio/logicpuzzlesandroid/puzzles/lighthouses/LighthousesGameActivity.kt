package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LighthousesGameActivity : GameGameActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState>() {
    private val document: LighthousesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LighthousesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LighthousesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LighthousesGame(level.layout, this, doc)
}