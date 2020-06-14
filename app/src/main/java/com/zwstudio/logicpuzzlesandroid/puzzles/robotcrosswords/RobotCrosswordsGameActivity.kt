package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class RobotCrosswordsGameActivity : GameGameActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState>() {
    @Bean
    protected lateinit var document: RobotCrosswordsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = RobotCrosswordsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        RobotCrosswordsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        RobotCrosswordsHelpActivity_.intent(this).start()
    }
}