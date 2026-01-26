package com.zwstudio.logicpuzzlesandroid.puzzles.mineslither

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MineSlitherMainActivity : GameMainActivity<MineSlitherGame, MineSlitherDocument, MineSlitherGameMove, MineSlitherGameState>() {
    private val document: MineSlitherDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MineSlitherOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MineSlitherGameActivity::class.java))
    }
}

class MineSlitherOptionsActivity : GameOptionsActivity<MineSlitherGame, MineSlitherDocument, MineSlitherGameMove, MineSlitherGameState>() {
    private val document: MineSlitherDocument by inject()
    override val doc get() = document
}

class MineSlitherHelpActivity : GameHelpActivity<MineSlitherGame, MineSlitherDocument, MineSlitherGameMove, MineSlitherGameState>() {
    private val document: MineSlitherDocument by inject()
    override val doc get() = document
}
