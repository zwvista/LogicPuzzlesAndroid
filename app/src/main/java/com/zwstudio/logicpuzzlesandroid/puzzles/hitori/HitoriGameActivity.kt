package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class HitoriGameActivity : GameGameActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    @Bean
    protected lateinit var document: HitoriDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = HitoriGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        HitoriGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        HitoriHelpActivity_.intent(this).start()
    }
}
