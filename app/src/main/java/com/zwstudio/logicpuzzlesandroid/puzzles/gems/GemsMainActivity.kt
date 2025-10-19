package com.zwstudio.logicpuzzlesandroid.puzzles.gems

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class GemsMainActivity : GameMainActivity<GemsGame, GemsDocument, GemsGameMove, GemsGameState>() {
    private val document: GemsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, GemsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, GemsGameActivity::class.java))
    }
}

class GemsOptionsActivity : GameOptionsActivity<GemsGame, GemsDocument, GemsGameMove, GemsGameState>() {
    private val document: GemsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class GemsHelpActivity : GameHelpActivity<GemsGame, GemsDocument, GemsGameMove, GemsGameState>() {
    private val document: GemsDocument by inject()
    override val doc get() = document
}