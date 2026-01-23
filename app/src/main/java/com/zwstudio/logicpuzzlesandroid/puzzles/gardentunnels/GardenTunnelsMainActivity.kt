package com.zwstudio.logicpuzzlesandroid.puzzles.gardentunnels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class GardenTunnelsMainActivity : GameMainActivity<GardenTunnelsGame, GardenTunnelsDocument, GardenTunnelsGameMove, GardenTunnelsGameState>() {
    private val document: GardenTunnelsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, GardenTunnelsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, GardenTunnelsGameActivity::class.java))
    }
}

class GardenTunnelsOptionsActivity : GameOptionsActivity<GardenTunnelsGame, GardenTunnelsDocument, GardenTunnelsGameMove, GardenTunnelsGameState>() {
    private val document: GardenTunnelsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class GardenTunnelsHelpActivity : GameHelpActivity<GardenTunnelsGame, GardenTunnelsDocument, GardenTunnelsGameMove, GardenTunnelsGameState>() {
    private val document: GardenTunnelsDocument by inject()
    override val doc get() = document
}