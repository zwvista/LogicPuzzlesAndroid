package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
open class KropkiGameActivity : GameGameActivity<KropkiGame?, KropkiDocument?, KropkiGameMove?, KropkiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KropkiDocument? = null
    override fun doc(): KropkiDocument {
        return document!!
    }

    protected var gameView: KropkiGameView? = null
    override fun getGameView(): View {
        return gameView!!
    }

    @AfterViews
    override fun init() {
        gameView = KropkiGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = KropkiGame(level.layout, "1" == level.settings["Bordered"], this, doc())
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
        KropkiHelpActivity_.intent(this).start()
    }
}