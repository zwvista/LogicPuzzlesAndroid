package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TheOddBrickMainActivity : GameMainActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    private val document: TheOddBrickDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TheOddBrickOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TheOddBrickGameActivity::class.java))
    }
}

class TheOddBrickOptionsActivity : GameOptionsActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    private val document: TheOddBrickDocument by inject()
    override val doc get() = document
}

class TheOddBrickHelpActivity : GameHelpActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    private val document: TheOddBrickDocument by inject()
    override val doc get() = document
}