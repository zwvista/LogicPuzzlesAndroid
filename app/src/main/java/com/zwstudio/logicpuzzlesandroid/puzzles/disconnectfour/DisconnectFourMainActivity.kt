package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class DisconnectFourMainActivity : GameMainActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    private val document: DisconnectFourDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, DisconnectFourOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, DisconnectFourGameActivity::class.java))
    }
}

class DisconnectFourOptionsActivity : GameOptionsActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    private val document: DisconnectFourDocument by inject()
    override val doc get() = document
}

class DisconnectFourHelpActivity : GameHelpActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    private val document: DisconnectFourDocument by inject()
    override val doc get() = document
}