package com.zwstudio.logicpuzzlesandroid.puzzles.warehouse

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class WarehouseGameActivity : GameGameActivity<WarehouseGame, WarehouseDocument, WarehouseGameMove, WarehouseGameState>() {
    private val document: WarehouseDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = WarehouseGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, WarehouseHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        WarehouseGame(level.layout, this, doc)
}