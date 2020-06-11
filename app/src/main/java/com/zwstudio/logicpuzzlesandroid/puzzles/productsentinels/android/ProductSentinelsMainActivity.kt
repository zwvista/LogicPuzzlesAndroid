package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class ProductSentinelsMainActivity : GameMainActivity<ProductSentinelsGame?, ProductSentinelsDocument?, ProductSentinelsGameMove?, ProductSentinelsGameState?>() {
    @Bean
    protected var document: ProductSentinelsDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        ProductSentinelsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        ProductSentinelsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ProductSentinelsOptionsActivity : GameOptionsActivity<ProductSentinelsGame?, ProductSentinelsDocument?, ProductSentinelsGameMove?, ProductSentinelsGameState?>() {
    @Bean
    protected var document: ProductSentinelsDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class ProductSentinelsHelpActivity : GameHelpActivity<ProductSentinelsGame?, ProductSentinelsDocument?, ProductSentinelsGameMove?, ProductSentinelsGameState?>() {
    @Bean
    protected var document: ProductSentinelsDocument? = null
    override fun doc() = document
}