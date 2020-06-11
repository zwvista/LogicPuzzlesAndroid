package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.data.TapaDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapaGameActivity : GameGameActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override fun doc() = document

    protected lateinit var gameView2: TapaGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = TapaGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID: String = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.setText(selectedLevelID)
        updateSolutionUI()
        levelInitilizing = true
        game = TapaGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move: TapaGameMove = doc().loadMove(rec)
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
        TapaHelpActivity_.intent(this).start()
    }
}