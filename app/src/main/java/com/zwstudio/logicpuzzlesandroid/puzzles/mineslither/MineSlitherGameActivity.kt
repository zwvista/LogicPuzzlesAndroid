package com.zwstudio.logicpuzzlesandroid.puzzles.mineslither

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MineSlitherGameActivity : GameGameActivity<MineSlitherGame, MineSlitherDocument, MineSlitherGameMove, MineSlitherGameState>() {
    private val document: MineSlitherDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MineSlitherGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MineSlitherHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MineSlitherGame(level.layout, this, doc)
}
