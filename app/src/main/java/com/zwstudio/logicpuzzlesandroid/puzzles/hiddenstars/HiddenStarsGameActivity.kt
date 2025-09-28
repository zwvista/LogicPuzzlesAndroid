package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class HiddenStarsGameActivity : GameGameActivity<HiddenStarsGame, HiddenStarsDocument, HiddenStarsGameMove, HiddenStarsGameState>() {
    private val document: HiddenStarsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = HiddenStarsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HiddenStarsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        HiddenStarsGame(level.layout, this, doc)
}