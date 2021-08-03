package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ParkLakesMainActivity : GameMainActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    private val document: ParkLakesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ParkLakesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ParkLakesGameActivity::class.java))
    }
}

class ParkLakesOptionsActivity : GameOptionsActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    private val document: ParkLakesDocument by inject()
    override val doc get() = document
}

class ParkLakesHelpActivity : GameHelpActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    private val document: ParkLakesDocument by inject()
    override val doc get() = document
}