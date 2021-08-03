package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NoughtsAndCrossesMainActivity : GameMainActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    private val document: NoughtsAndCrossesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NoughtsAndCrossesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NoughtsAndCrossesGameActivity::class.java))
    }
}

class NoughtsAndCrossesOptionsActivity : GameOptionsActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    private val document: NoughtsAndCrossesDocument by inject()
    override val doc get() = document
}

class NoughtsAndCrossesHelpActivity : GameHelpActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    private val document: NoughtsAndCrossesDocument by inject()
    override val doc get() = document
}