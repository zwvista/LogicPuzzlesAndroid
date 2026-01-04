package com.zwstudio.logicpuzzlesandroid.puzzles.caffelatte

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CaffelatteMainActivity : GameMainActivity<CaffelatteGame, CaffelatteDocument, CaffelatteGameMove, CaffelatteGameState>() {
    private val document: CaffelatteDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CaffelatteOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CaffelatteGameActivity::class.java))
    }
}

class CaffelatteOptionsActivity : GameOptionsActivity<CaffelatteGame, CaffelatteDocument, CaffelatteGameMove, CaffelatteGameState>() {
    private val document: CaffelatteDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CaffelatteHelpActivity : GameHelpActivity<CaffelatteGame, CaffelatteDocument, CaffelatteGameMove, CaffelatteGameState>() {
    private val document: CaffelatteDocument by inject()
    override val doc get() = document
}