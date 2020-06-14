package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TataminoGameActivity : GameGameActivity<TataminoGame, TataminoDocument, TataminoGameMove, TataminoGameState>() {
    @Bean
    protected lateinit var document: TataminoDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TataminoGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TataminoGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TataminoHelpActivity_.intent(this).start()
    }
}