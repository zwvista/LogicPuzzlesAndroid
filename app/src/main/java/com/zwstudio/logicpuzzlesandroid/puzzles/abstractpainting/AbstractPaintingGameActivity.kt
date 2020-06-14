package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class AbstractPaintingGameActivity : GameGameActivity<AbstractPaintingGame, AbstractPaintingDocument, AbstractPaintingGameMove, AbstractPaintingGameState>() {
    @Bean
    protected lateinit var document: AbstractPaintingDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = AbstractPaintingGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        AbstractPaintingGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        AbstractPaintingHelpActivity_.intent(this).start()
    }
}
