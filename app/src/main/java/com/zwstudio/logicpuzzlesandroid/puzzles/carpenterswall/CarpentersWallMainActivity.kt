package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CarpentersWallMainActivity : GameMainActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    private val document: CarpentersWallDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CarpentersWallOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CarpentersWallGameActivity::class.java))
    }
}

class CarpentersWallOptionsActivity : GameOptionsActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    private val document: CarpentersWallDocument by inject()
    override val doc get() = document
}

class CarpentersWallHelpActivity : GameHelpActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    private val document: CarpentersWallDocument by inject()
    override val doc get() = document
}