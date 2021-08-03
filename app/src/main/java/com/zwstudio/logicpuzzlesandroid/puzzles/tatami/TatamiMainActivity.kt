package com.zwstudio.logicpuzzlesandroid.puzzles.tatami

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TatamiMainActivity : GameMainActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    private val document: TatamiDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TatamiOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TatamiGameActivity::class.java))
    }
}

class TatamiOptionsActivity : GameOptionsActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    private val document: TatamiDocument by inject()
    override val doc get() = document
}

class TatamiHelpActivity : GameHelpActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState>() {
    private val document: TatamiDocument by inject()
    override val doc get() = document
}