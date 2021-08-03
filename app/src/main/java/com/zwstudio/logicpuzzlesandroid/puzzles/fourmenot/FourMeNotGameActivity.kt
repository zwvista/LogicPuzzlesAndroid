package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FourMeNotGameActivity : GameGameActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState>() {
    private val document: FourMeNotDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FourMeNotGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FourMeNotHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FourMeNotGame(level.layout, this, doc)
}