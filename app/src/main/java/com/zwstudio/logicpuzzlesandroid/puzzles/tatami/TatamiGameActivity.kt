package com.zwstudio.logicpuzzlesandroid.puzzles.tatami

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TatamiGameActivity : GameGameActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    @Bean
    protected lateinit var document: TatamiDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TatamiGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TatamiGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TatamiHelpActivity_.intent(this).start()
    }
}