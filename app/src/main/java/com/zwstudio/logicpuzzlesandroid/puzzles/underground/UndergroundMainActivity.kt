package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class UndergroundMainActivity : GameMainActivity<UndergroundGame, UndergroundDocument, UndergroundGameMove, UndergroundGameState>() {
    private val document: UndergroundDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, UndergroundOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, UndergroundGameActivity::class.java))
    }
}

class UndergroundOptionsActivity : GameOptionsActivity<UndergroundGame, UndergroundDocument, UndergroundGameMove, UndergroundGameState>() {
    private val document: UndergroundDocument by inject()
    override val doc get() = document
}

class UndergroundHelpActivity : GameHelpActivity<UndergroundGame, UndergroundDocument, UndergroundGameMove, UndergroundGameState>() {
    private val document: UndergroundDocument by inject()
    override val doc get() = document
}