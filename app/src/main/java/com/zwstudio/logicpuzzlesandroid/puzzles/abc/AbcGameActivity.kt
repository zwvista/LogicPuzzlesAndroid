package com.zwstudio.logicpuzzlesandroid.puzzles.abc

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class AbcGameActivity : GameGameActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    private val document: AbcDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = AbcGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, AbcHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        AbcGame(level.layout, this, doc)
}
