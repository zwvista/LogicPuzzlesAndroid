package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class TapaIslandsGameActivity : GameGameActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    @Bean
    protected lateinit var document: TapaIslandsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = TapaIslandsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        TapaIslandsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        TapaIslandsHelpActivity_.intent(this).start()
    }
}