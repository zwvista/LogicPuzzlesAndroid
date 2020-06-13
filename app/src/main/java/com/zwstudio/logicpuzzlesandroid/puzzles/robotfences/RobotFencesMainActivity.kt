package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class RobotFencesMainActivity : GameMainActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState>() {
    @Bean
    protected lateinit var document: RobotFencesDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        RobotFencesOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        RobotFencesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class RobotFencesOptionsActivity : GameOptionsActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState>() {
    @Bean
    protected lateinit var document: RobotFencesDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class RobotFencesHelpActivity : GameHelpActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState>() {
    @Bean
    protected lateinit var document: RobotFencesDocument
    override val doc get() = document
}