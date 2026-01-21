package com.zwstudio.logicpuzzlesandroid.puzzles.pleasecomeback

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PleaseComeBackMainActivity : GameMainActivity<PleaseComeBackGame, PleaseComeBackDocument, PleaseComeBackGameMove, PleaseComeBackGameState>() {
    private val document: PleaseComeBackDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PleaseComeBackOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PleaseComeBackGameActivity::class.java))
    }
}

class PleaseComeBackOptionsActivity : GameOptionsActivity<PleaseComeBackGame, PleaseComeBackDocument, PleaseComeBackGameMove, PleaseComeBackGameState>() {
    private val document: PleaseComeBackDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class PleaseComeBackHelpActivity : GameHelpActivity<PleaseComeBackGame, PleaseComeBackDocument, PleaseComeBackGameMove, PleaseComeBackGameState>() {
    private val document: PleaseComeBackDocument by inject()
    override val doc get() = document
}