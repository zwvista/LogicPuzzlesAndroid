package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
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

    protected override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = RobotFencesGame(level.layout, this, doc)
        try {
            // restore game state
            for (rec in doc.moveProgress()) {
                val move = doc.loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc.levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        RobotFencesHelpActivity_.intent(this).start()
    }
}