package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TapaGameActivity : GameGameActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    private val document: TapaDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TapaGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TapaHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TapaGame(level.layout, this, doc)
}