package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class LitsGameActivity : GameGameActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState>() {
    @Bean
    protected lateinit var document: LitsDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = LitsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        LitsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        LitsHelpActivity_.intent(this).start()
    }
}