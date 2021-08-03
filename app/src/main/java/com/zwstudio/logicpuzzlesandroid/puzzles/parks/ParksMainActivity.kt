package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ParksMainActivity : GameMainActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    private val document: ParksDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ParksOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ParksGameActivity::class.java))
    }
}

class ParksOptionsActivity : GameOptionsActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    private val document: ParksDocument by inject()
    override val doc get() = document
}

class ParksHelpActivity : GameHelpActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    private val document: ParksDocument by inject()
    override val doc get() = document
}