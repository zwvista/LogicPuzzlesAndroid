package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TapAlikeMainActivity : GameMainActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    private val document: TapAlikeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TapAlikeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TapAlikeGameActivity::class.java))
    }
}

class TapAlikeOptionsActivity : GameOptionsActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    private val document: TapAlikeDocument by inject()
    override val doc get() = document
}

class TapAlikeHelpActivity : GameHelpActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    private val document: TapAlikeDocument by inject()
    override val doc get() = document
}