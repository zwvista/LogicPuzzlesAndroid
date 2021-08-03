package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BWTapaGameActivity : GameGameActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    private val document: BWTapaDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BWTapaGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BWTapaHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BWTapaGame(level.layout, this, doc)
}