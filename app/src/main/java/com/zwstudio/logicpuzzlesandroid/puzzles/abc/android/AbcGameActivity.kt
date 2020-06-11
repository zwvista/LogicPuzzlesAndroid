package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class AbcGameActivity : GameGameActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    @Bean
    protected lateinit var document: AbcDocument
    override fun doc() = document

    protected lateinit var gameView2: AbcGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = AbcGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = AbcGame(level.layout, this, doc())
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
        AbcHelpActivity_.intent(this).start()
    }
}
