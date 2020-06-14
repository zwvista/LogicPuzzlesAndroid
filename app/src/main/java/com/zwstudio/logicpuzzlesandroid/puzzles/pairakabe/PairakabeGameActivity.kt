package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class PairakabeGameActivity : GameGameActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = PairakabeGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        PairakabeGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        PairakabeHelpActivity_.intent(this).start()
    }
}