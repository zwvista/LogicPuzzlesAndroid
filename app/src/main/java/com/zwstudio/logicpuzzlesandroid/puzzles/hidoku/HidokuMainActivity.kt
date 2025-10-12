package com.zwstudio.logicpuzzlesandroid.puzzles.hidoku

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HidokuMainActivity : GameMainActivity<HidokuGame, HidokuDocument, HidokuGameMove, HidokuGameState>() {
    private val document: HidokuDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HidokuOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HidokuGameActivity::class.java))
    }
}

class HidokuOptionsActivity : GameOptionsActivity<HidokuGame, HidokuDocument, HidokuGameMove, HidokuGameState>() {
    private val document: HidokuDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class HidokuHelpActivity : GameHelpActivity<HidokuGame, HidokuDocument, HidokuGameMove, HidokuGameState>() {
    private val document: HidokuDocument by inject()
    override val doc get() = document
}
