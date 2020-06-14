package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class GardenerGameActivity : GameGameActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    @Bean
    protected lateinit var document: GardenerDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = GardenerGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        GardenerGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        GardenerHelpActivity_.intent(this).start()
    }
}