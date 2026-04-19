package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TheGreyLabyrinthMainActivity : GameMainActivity<TheGreyLabyrinthGame, TheGreyLabyrinthDocument, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>() {
    private val document: TheGreyLabyrinthDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TheGreyLabyrinthOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TheGreyLabyrinthGameActivity::class.java))
    }
}

class TheGreyLabyrinthOptionsActivity : GameOptionsActivity<TheGreyLabyrinthGame, TheGreyLabyrinthDocument, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>() {
    private val document: TheGreyLabyrinthDocument by inject()
    override val doc get() = document
}

class TheGreyLabyrinthHelpActivity : GameHelpActivity<TheGreyLabyrinthGame, TheGreyLabyrinthDocument, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>() {
    private val document: TheGreyLabyrinthDocument by inject()
    override val doc get() = document
}