package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ParksGameActivity : GameGameActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    @Bean
    protected lateinit var document: ParksDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = ParksGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel): ParksGame {
        val treesInEachArea = (level.settings["TreesInEachArea"] ?: "1").toInt()
        return ParksGame(level.layout, treesInEachArea, this, doc)
    }

    @Click
    protected fun btnHelp() {
        ParksHelpActivity_.intent(this).start()
    }
}