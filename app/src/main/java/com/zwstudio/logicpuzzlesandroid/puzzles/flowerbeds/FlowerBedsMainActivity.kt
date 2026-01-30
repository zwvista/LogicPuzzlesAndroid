package com.zwstudio.logicpuzzlesandroid.puzzles.flowerbeds

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FlowerBedsMainActivity : GameMainActivity<FlowerBedsGame, FlowerBedsDocument, FlowerBedsGameMove, FlowerBedsGameState>() {
    private val document: FlowerBedsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FlowerBedsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FlowerBedsGameActivity::class.java))
    }
}

class FlowerBedsOptionsActivity : GameOptionsActivity<FlowerBedsGame, FlowerBedsDocument, FlowerBedsGameMove, FlowerBedsGameState>() {
    private val document: FlowerBedsDocument by inject()
    override val doc get() = document
}

class FlowerBedsHelpActivity : GameHelpActivity<FlowerBedsGame, FlowerBedsDocument, FlowerBedsGameMove, FlowerBedsGameState>() {
    private val document: FlowerBedsDocument by inject()
    override val doc get() = document
}
