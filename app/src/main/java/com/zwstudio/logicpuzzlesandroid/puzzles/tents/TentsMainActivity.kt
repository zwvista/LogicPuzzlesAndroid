package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TentsMainActivity : GameMainActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    private val document: TentsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TentsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TentsGameActivity::class.java))
    }
}

class TentsOptionsActivity : GameOptionsActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    private val document: TentsDocument by inject()
    override val doc get() = document
}

class TentsHelpActivity : GameHelpActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState>() {
    private val document: TentsDocument by inject()
    override val doc get() = document
}