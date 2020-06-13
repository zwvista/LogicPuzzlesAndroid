package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class ProductSentinelsMainActivity : GameMainActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    @Bean
    protected lateinit var document: ProductSentinelsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        ProductSentinelsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        ProductSentinelsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ProductSentinelsOptionsActivity : GameOptionsActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    @Bean
    protected lateinit var document: ProductSentinelsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class ProductSentinelsHelpActivity : GameHelpActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    @Bean
    protected lateinit var document: ProductSentinelsDocument
    override val doc get() = document
}