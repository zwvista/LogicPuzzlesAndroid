package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ShopAndGasGameActivity : GameGameActivity<ShopAndGasGame, ShopAndGasDocument, ShopAndGasGameMove, ShopAndGasGameState>() {
    private val document: ShopAndGasDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ShopAndGasGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ShopAndGasHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ShopAndGasGame(level.layout, this, doc)
}