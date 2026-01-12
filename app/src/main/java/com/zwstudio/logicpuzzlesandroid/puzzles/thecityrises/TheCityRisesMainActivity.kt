package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TheCityRisesMainActivity : GameMainActivity<TheCityRisesGame, TheCityRisesDocument, TheCityRisesGameMove, TheCityRisesGameState>() {
    private val document: TheCityRisesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TheCityRisesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TheCityRisesGameActivity::class.java))
    }
}

class TheCityRisesOptionsActivity : GameOptionsActivity<TheCityRisesGame, TheCityRisesDocument, TheCityRisesGameMove, TheCityRisesGameState>() {
    private val document: TheCityRisesDocument by inject()
    override val doc get() = document
}

class TheCityRisesHelpActivity : GameHelpActivity<TheCityRisesGame, TheCityRisesDocument, TheCityRisesGameMove, TheCityRisesGameState>() {
    private val document: TheCityRisesDocument by inject()
    override val doc get() = document
}