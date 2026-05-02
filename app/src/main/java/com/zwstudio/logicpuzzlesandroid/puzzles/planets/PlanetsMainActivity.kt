package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PlanetsMainActivity : GameMainActivity<PlanetsGame, PlanetsDocument, PlanetsGameMove, PlanetsGameState>() {
    private val document: PlanetsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PlanetsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PlanetsGameActivity::class.java))
    }
}

class PlanetsOptionsActivity : GameOptionsActivity<PlanetsGame, PlanetsDocument, PlanetsGameMove, PlanetsGameState>() {
    private val document: PlanetsDocument by inject()
    override val doc get() = document
}

class PlanetsHelpActivity : GameHelpActivity<PlanetsGame, PlanetsDocument, PlanetsGameMove, PlanetsGameState>() {
    private val document: PlanetsDocument by inject()
    override val doc get() = document
}