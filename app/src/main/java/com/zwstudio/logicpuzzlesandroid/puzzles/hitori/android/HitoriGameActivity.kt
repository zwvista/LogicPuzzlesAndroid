package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class HitoriGameActivity : GameGameActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    @Bean
    protected lateinit var document: HitoriDocument
    override fun doc() = document

    @AfterViews
    override fun init() {
        gameView = HitoriGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()

        levelInitilizing = true
        game = HitoriGame(level.layout, this, doc())
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
        HitoriHelpActivity_.intent(this).start()
    }
}
