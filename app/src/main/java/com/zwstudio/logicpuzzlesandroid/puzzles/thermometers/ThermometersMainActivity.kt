package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ThermometersMainActivity : GameMainActivity<ThermometersGame, ThermometersDocument, ThermometersGameMove, ThermometersGameState>() {
    private val document: ThermometersDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ThermometersOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ThermometersGameActivity::class.java))
    }
}

class ThermometersOptionsActivity : GameOptionsActivity<ThermometersGame, ThermometersDocument, ThermometersGameMove, ThermometersGameState>() {
    private val document: ThermometersDocument by inject()
    override val doc get() = document
}

class ThermometersHelpActivity : GameHelpActivity<ThermometersGame, ThermometersDocument, ThermometersGameMove, ThermometersGameState>() {
    private val document: ThermometersDocument by inject()
    override val doc get() = document
}