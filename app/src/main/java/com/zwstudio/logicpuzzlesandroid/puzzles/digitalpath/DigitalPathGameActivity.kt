package com.zwstudio.logicpuzzlesandroid.puzzles.digitalpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class DigitalPathGameActivity : GameGameActivity<DigitalPathGame, DigitalPathDocument, DigitalPathGameMove, DigitalPathGameState>() {
    private val document: DigitalPathDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = DigitalPathGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, DigitalPathHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        DigitalPathGame(level.layout, this, doc)
}