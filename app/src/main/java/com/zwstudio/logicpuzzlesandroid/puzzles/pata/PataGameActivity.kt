package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PataGameActivity : GameGameActivity<PataGame, PataDocument, PataGameMove, PataGameState>() {
    @Bean
    protected lateinit var document: PataDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = PataGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        PataGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        PataHelpActivity_.intent(this).start()
    }
}