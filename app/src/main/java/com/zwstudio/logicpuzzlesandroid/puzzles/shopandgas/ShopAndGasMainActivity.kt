package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ShopAndGasMainActivity : GameMainActivity<ShopAndGasGame, ShopAndGasDocument, ShopAndGasGameMove, ShopAndGasGameState>() {
    private val document: ShopAndGasDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ShopAndGasOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ShopAndGasGameActivity::class.java))
    }
}

class ShopAndGasOptionsActivity : GameOptionsActivity<ShopAndGasGame, ShopAndGasDocument, ShopAndGasGameMove, ShopAndGasGameState>() {
    private val document: ShopAndGasDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class ShopAndGasHelpActivity : GameHelpActivity<ShopAndGasGame, ShopAndGasDocument, ShopAndGasGameMove, ShopAndGasGameState>() {
    private val document: ShopAndGasDocument by inject()
    override val doc get() = document
}