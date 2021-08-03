package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TennerGridMainActivity : GameMainActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    private val document: TennerGridDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TennerGridOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TennerGridGameActivity::class.java))
    }
}

class TennerGridOptionsActivity : GameOptionsActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    private val document: TennerGridDocument by inject()
    override val doc get() = document
}

class TennerGridHelpActivity : GameHelpActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    private val document: TennerGridDocument by inject()
    override val doc get() = document
}