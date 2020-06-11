package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data.PairakabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PairakabeGameActivity : GameGameActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override fun doc() = document

    protected lateinit var gameView2: PairakabeGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = PairakabeGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = PairakabeGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: PairakabeGameMove = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex: Int = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        PairakabeHelpActivity_.intent(this).start()
    }
}