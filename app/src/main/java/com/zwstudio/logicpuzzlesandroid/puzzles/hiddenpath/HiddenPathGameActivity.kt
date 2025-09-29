package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class HiddenPathGameActivity : GameGameActivity<HiddenPathGame, HiddenPathDocument, HiddenPathGameMove, HiddenPathGameState>() {
    private val document: HiddenPathDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = HiddenPathGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HiddenPathHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        HiddenPathGame(level.layout, this, doc)
}
