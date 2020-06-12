package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.data.BalancedTapasDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BalancedTapasGameActivity : GameGameActivity<BalancedTapasGame, BalancedTapasDocument, BalancedTapasGameMove, BalancedTapasGameState>() {
    @Bean
    protected lateinit var document: BalancedTapasDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = BalancedTapasGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = BalancedTapasGame(level.layout, level.settings["LeftPart"]!!, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        BalancedTapasHelpActivity_.intent(this).start()
    }
}
