package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data.NumberLinkDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NumberLinkGameActivity : GameGameActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    @Bean
    protected lateinit var document: NumberLinkDocument
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        gameView = NumberLinkGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = NumberLinkGame(level.layout, this, doc())
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
        NumberLinkHelpActivity_.intent(this).start()
    }
}