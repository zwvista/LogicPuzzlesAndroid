package com.zwstudio.logicpuzzlesandroid.puzzles.islandconnections

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class IslandConnectionsMainActivity : GameMainActivity<IslandConnectionsGame, IslandConnectionsDocument, IslandConnectionsGameMove, IslandConnectionsGameState>() {
    private val document: IslandConnectionsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, IslandConnectionsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, IslandConnectionsGameActivity::class.java))
    }
}

class IslandConnectionsOptionsActivity : GameOptionsActivity<IslandConnectionsGame, IslandConnectionsDocument, IslandConnectionsGameMove, IslandConnectionsGameState>() {
    private val document: IslandConnectionsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class IslandConnectionsHelpActivity : GameHelpActivity<IslandConnectionsGame, IslandConnectionsDocument, IslandConnectionsGameMove, IslandConnectionsGameState>() {
    private val document: IslandConnectionsDocument by inject()
    override val doc get() = document
}