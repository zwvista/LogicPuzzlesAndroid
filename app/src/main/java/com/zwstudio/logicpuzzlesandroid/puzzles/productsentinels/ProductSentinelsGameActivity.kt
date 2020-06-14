package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class ProductSentinelsGameActivity : GameGameActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    @Bean
    protected lateinit var document: ProductSentinelsDocument
    override val doc get() = document

    @AfterViews
    protected override fun init() {
        gameView = ProductSentinelsGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        ProductSentinelsGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        ProductSentinelsHelpActivity_.intent(this).start()
    }
}