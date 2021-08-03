package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TapARowMainActivity : GameMainActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    private val document: TapARowDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TapARowOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TapARowGameActivity::class.java))
    }
}

class TapARowOptionsActivity : GameOptionsActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    private val document: TapARowDocument by inject()
    override val doc get() = document
}

class TapARowHelpActivity : GameHelpActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState>() {
    private val document: TapARowDocument by inject()
    override val doc get() = document
}