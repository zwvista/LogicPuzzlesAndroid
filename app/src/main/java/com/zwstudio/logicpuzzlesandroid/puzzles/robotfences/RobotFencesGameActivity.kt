package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class RobotFencesGameActivity : GameGameActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState>() {
    @Bean
    protected lateinit var document: RobotFencesDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = RobotFencesGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        RobotFencesGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        RobotFencesHelpActivity_.intent(this).start()
    }
}