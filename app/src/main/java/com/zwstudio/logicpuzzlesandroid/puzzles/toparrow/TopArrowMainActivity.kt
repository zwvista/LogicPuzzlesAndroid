package com.zwstudio.logicpuzzlesandroid.puzzles.toparrow

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TopArrowMainActivity : GameMainActivity<TopArrowGame, TopArrowDocument, TopArrowGameMove, TopArrowGameState>() {
    private val document: TopArrowDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TopArrowOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TopArrowGameActivity::class.java))
    }
}

class TopArrowOptionsActivity : GameOptionsActivity<TopArrowGame, TopArrowDocument, TopArrowGameMove, TopArrowGameState>() {
    private val document: TopArrowDocument by inject()
    override val doc get() = document
}

class TopArrowHelpActivity : GameHelpActivity<TopArrowGame, TopArrowDocument, TopArrowGameMove, TopArrowGameState>() {
    private val document: TopArrowDocument by inject()
    override val doc get() = document
}