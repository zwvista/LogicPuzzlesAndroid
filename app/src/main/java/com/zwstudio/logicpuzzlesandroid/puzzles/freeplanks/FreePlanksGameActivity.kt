package com.zwstudio.logicpuzzlesandroid.puzzles.freeplanks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FreePlanksGameActivity : GameGameActivity<FreePlanksGame, FreePlanksDocument, FreePlanksGameMove, FreePlanksGameState>() {
    private val document: FreePlanksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FreePlanksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FreePlanksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FreePlanksGame(level.layout, this, doc)
}