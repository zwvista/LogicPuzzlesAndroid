package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data.KakuroDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class KakuroGameActivity : GameGameActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    @Bean
    protected lateinit var document: KakuroDocument
    override fun doc() = document

    protected lateinit var gameView2: KakuroGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = KakuroGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = KakuroGame(level.layout, this, doc())
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
        KakuroHelpActivity_.intent(this).start()
    }
}