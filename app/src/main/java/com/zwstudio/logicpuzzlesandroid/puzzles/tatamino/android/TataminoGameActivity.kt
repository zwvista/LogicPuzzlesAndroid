package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.data.TataminoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TataminoGameActivity : GameGameActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    @Bean
    protected lateinit var document: TataminoDocument
    override fun doc() = document

    protected lateinit var gameView2: TataminoGameView
    override fun getGameView() = gameView2

    @AfterViews
    protected override fun init() {
        gameView2 = TataminoGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = TataminoGame(level.layout, this, doc())
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
        TataminoHelpActivity_.intent(this).start()
    }
}