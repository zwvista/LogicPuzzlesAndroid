package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BusySeasGameActivity : GameGameActivity<BusySeasGame, BusySeasDocument, BusySeasGameMove, BusySeasGameState>() {
    @Bean
    protected lateinit var document: BusySeasDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = BusySeasGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc.selectedLevelID
        val level = doc.levels[doc.levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = BusySeasGame(level.layout, this, doc)
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
        BusySeasHelpActivity_.intent(this).start()
    }
}