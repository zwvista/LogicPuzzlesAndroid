package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FutoshikiGameActivity : GameGameActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    private val document: FutoshikiDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FutoshikiGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FutoshikiHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FutoshikiGame(level.layout, this, doc)
}