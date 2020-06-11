package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data.RippleEffectDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class RippleEffectGameActivity : GameGameActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState>() {
    @Bean
    protected lateinit var document: RippleEffectDocument
    override fun doc() = document

    protected lateinit var gameView2: RippleEffectGameView
    override fun getGameView() = gameView2

    @AfterViews
    override fun init() {
        gameView2 = RippleEffectGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[doc().levels.indexOfFirst { it.id == selectedLevelID }.coerceAtLeast(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = RippleEffectGame(level.layout, this, doc())
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
        RippleEffectHelpActivity_.intent(this).start()
    }
}