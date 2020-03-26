package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data.FenceLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
open class FenceLitsGameActivity : GameGameActivity<FenceLitsGame?, FenceLitsDocument?, FenceLitsGameMove?, FenceLitsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceLitsDocument? = null
    override fun doc(): FenceLitsDocument {
        return document!!
    }

    protected var gameView: FenceLitsGameView? = null
    override fun getGameView(): View {
        return gameView!!
    }

    @AfterViews
    override fun init() {
        gameView = FenceLitsGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = FenceLitsGame(level.layout, this, doc())
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
        FenceLitsHelpActivity_.intent(this).start()
    }
}