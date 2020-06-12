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

    @AfterViews
    protected override fun init() {
        gameView = TapaGameView(this)
        super.init()
    }

    protected override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = TapaGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex in 0 until game.moveCount)
                while (moveIndex != game.moveIndex)
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