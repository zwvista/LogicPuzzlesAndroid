package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FenceLitsGameActivity : GameGameActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState>() {
    private val document: FenceLitsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FenceLitsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FenceLitsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FenceLitsGame(level.layout, this, doc)
}