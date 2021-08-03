package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class RobotCrosswordsMainActivity : GameMainActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    private val document: RobotCrosswordsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, RobotCrosswordsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, RobotCrosswordsGameActivity::class.java))
    }
}

class RobotCrosswordsOptionsActivity : GameOptionsActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    private val document: RobotCrosswordsDocument by inject()
    override val doc get() = document
}

class RobotCrosswordsHelpActivity : GameHelpActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    private val document: RobotCrosswordsDocument by inject()
    override val doc get() = document
}