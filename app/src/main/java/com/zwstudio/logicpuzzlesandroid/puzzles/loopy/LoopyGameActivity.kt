package com.zwstudio.logicpuzzlesandroid.puzzles.loopy

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LoopyGameActivity : GameGameActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState>() {
    @Bean
    protected lateinit var document: LoopyDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = LoopyGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        LoopyGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        LoopyHelpActivity_.intent(this).start()
    }
}