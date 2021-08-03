package com.zwstudio.logicpuzzlesandroid.puzzles.bridges

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BridgesMainActivity : GameMainActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    private val document: BridgesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BridgesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BridgesGameActivity::class.java))
    }
}

class BridgesOptionsActivity : GameOptionsActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    private val document: BridgesDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class BridgesHelpActivity : GameHelpActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState>() {
    private val document: BridgesDocument by inject()
    override val doc get() = document
}