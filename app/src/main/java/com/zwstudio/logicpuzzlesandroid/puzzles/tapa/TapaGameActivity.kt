package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapaGameActivity : GameGameActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TapaGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TapaGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TapaHelpActivity_.intent(this).start()
    }
}