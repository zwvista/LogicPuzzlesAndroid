package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.data.RobotCrosswordsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class RobotCrosswordsMainActivity : GameMainActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    @Bean
    protected lateinit var document: RobotCrosswordsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        RobotCrosswordsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        RobotCrosswordsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class RobotCrosswordsOptionsActivity : GameOptionsActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    @Bean
    protected lateinit var document: RobotCrosswordsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_options)
class RobotCrosswordsOptionsActivity : GameOptionsActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    @Bean
    protected lateinit var document: RobotCrosswordsDocument
    override fun doc() = document
}