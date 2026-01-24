package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class VeniceMainActivity : GameMainActivity<VeniceGame, VeniceDocument, VeniceGameMove, VeniceGameState>() {
    private val document: VeniceDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, VeniceOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, VeniceGameActivity::class.java))
    }
}

class VeniceOptionsActivity : GameOptionsActivity<VeniceGame, VeniceDocument, VeniceGameMove, VeniceGameState>() {
    private val document: VeniceDocument by inject()
    override val doc get() = document
}

class VeniceHelpActivity : GameHelpActivity<VeniceGame, VeniceDocument, VeniceGameMove, VeniceGameState>() {
    private val document: VeniceDocument by inject()
    override val doc get() = document
}