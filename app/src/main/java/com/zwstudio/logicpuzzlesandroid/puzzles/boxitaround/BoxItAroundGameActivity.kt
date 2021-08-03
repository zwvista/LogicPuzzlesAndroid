package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BoxItAroundGameActivity : GameGameActivity<BoxItAroundGame, BoxItAroundDocument, BoxItAroundGameMove, BoxItAroundGameState>() {
    private val document: BoxItAroundDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BoxItAroundGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BoxItAroundHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BoxItAroundGame(level.layout, this, doc)
}