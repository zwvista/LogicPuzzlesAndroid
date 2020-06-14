package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class FutoshikiGameActivity : GameGameActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState>() {
    @Bean
    protected lateinit var document: FutoshikiDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = FutoshikiGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        FutoshikiGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        FutoshikiHelpActivity_.intent(this).start()
    }
}