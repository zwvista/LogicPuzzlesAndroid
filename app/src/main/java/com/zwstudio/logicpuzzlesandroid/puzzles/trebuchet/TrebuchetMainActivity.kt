package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TrebuchetMainActivity : GameMainActivity<TrebuchetGame, TrebuchetDocument, TrebuchetGameMove, TrebuchetGameState>() {
    private val document: TrebuchetDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TrebuchetOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TrebuchetGameActivity::class.java))
    }
}

class TrebuchetOptionsActivity : GameOptionsActivity<TrebuchetGame, TrebuchetDocument, TrebuchetGameMove, TrebuchetGameState>() {
    private val document: TrebuchetDocument by inject()
    override val doc get() = document
}

class TrebuchetHelpActivity : GameHelpActivity<TrebuchetGame, TrebuchetDocument, TrebuchetGameMove, TrebuchetGameState>() {
    private val document: TrebuchetDocument by inject()
    override val doc get() = document
}