package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ProductSentinelsMainActivity : GameMainActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    private val document: ProductSentinelsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ProductSentinelsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ProductSentinelsGameActivity::class.java))
    }
}

class ProductSentinelsOptionsActivity : GameOptionsActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    private val document: ProductSentinelsDocument by inject()
    override val doc get() = document
}

class ProductSentinelsHelpActivity : GameHelpActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState>() {
    private val document: ProductSentinelsDocument by inject()
    override val doc get() = document
}