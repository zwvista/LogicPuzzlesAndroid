package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class WallsMainActivity : GameMainActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    private val document: WallsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, WallsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, WallsGameActivity::class.java))
    }
}

class WallsOptionsActivity : GameOptionsActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    private val document: WallsDocument by inject()
    override val doc get() = document
}

class WallsHelpActivity : GameHelpActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    private val document: WallsDocument by inject()
    override val doc get() = document
}