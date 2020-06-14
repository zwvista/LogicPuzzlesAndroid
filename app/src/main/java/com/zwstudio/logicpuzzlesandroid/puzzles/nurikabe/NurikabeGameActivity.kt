package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class NurikabeGameActivity : GameGameActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState>() {
    @Bean
    protected lateinit var document: NurikabeDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = NurikabeGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        NurikabeGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        NurikabeHelpActivity_.intent(this).start()
    }
}