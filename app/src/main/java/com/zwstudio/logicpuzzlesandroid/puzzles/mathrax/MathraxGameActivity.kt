package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class MathraxGameActivity : GameGameActivity<MathraxGame, MathraxDocument, MathraxGameMove, MathraxGameState>() {
    @Bean
    protected lateinit var document: MathraxDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = MathraxGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        MathraxGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        MathraxHelpActivity_.intent(this).start()
    }
}