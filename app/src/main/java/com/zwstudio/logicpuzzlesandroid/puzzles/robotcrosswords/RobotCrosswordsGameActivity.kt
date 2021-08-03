package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class RobotCrosswordsGameActivity : GameGameActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    private val document: RobotCrosswordsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = RobotCrosswordsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, RobotCrosswordsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        RobotCrosswordsGame(level.layout, this, doc)
}