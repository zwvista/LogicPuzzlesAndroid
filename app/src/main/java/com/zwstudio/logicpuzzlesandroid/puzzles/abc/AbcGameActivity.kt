package com.zwstudio.logicpuzzlesandroid.puzzles.abc

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class AbcGameActivity : GameGameActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    @Bean
    protected lateinit var document: AbcDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = AbcGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        AbcGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        AbcHelpActivity_.intent(this).start()
    }
}
