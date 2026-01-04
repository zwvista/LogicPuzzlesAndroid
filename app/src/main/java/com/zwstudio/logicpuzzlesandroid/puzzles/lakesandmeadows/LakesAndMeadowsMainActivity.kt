package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LakesAndMeadowsMainActivity : GameMainActivity<LakesAndMeadowsGame, LakesAndMeadowsDocument, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>() {
    private val document: LakesAndMeadowsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LakesAndMeadowsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LakesAndMeadowsGameActivity::class.java))
    }
}

class LakesAndMeadowsOptionsActivity : GameOptionsActivity<LakesAndMeadowsGame, LakesAndMeadowsDocument, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>() {
    private val document: LakesAndMeadowsDocument by inject()
    override val doc get() = document
}

class LakesAndMeadowsHelpActivity : GameHelpActivity<LakesAndMeadowsGame, LakesAndMeadowsDocument, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>() {
    private val document: LakesAndMeadowsDocument by inject()
    override val doc get() = document
}
