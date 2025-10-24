package com.zwstudio.logicpuzzlesandroid.puzzles.planks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PlanksGameActivity : GameGameActivity<PlanksGame, PlanksDocument, PlanksGameMove, PlanksGameState>() {
    private val document: PlanksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PlanksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PlanksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PlanksGame(level.layout, this, doc)
}