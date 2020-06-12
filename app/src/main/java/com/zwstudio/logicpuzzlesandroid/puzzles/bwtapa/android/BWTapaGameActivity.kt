package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.data.BWTapaDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class BWTapaGameActivity : GameGameActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    @Bean
    protected lateinit var document: BWTapaDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = BWTapaGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = BWTapaGame(level.layout, this, doc())
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
        BWTapaHelpActivity_.intent(this).start()
    }
}