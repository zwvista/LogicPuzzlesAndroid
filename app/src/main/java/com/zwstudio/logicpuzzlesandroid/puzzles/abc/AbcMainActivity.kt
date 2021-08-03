package com.zwstudio.logicpuzzlesandroid.puzzles.abc

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class AbcMainActivity : GameMainActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    private val document: AbcDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, AbcOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, AbcGameActivity::class.java))
    }
}

class AbcOptionsActivity : GameOptionsActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    private val document: AbcDocument by inject()
    override val doc get() = document
}

class AbcHelpActivity : GameHelpActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    private val document: AbcDocument by inject()
    override val doc get() = document
}
