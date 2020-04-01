package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LoopyGameActivity : GameGameActivity<LoopyGame?, LoopyDocument?, LoopyGameMove?, LoopyGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LoopyDocument? = null
    override fun doc(): LoopyDocument {
        return document!!
    }

    protected var gameView: LoopyGameView? = null
    override fun getGameView(): View {
        return gameView!!
    }

    @AfterViews
    override fun init() {
        gameView = LoopyGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = LoopyGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game!!.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game!!.moveCount()) while (moveIndex != game!!.moveIndex()) game!!.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        LoopyHelpActivity_.intent(this).start()
    }
}