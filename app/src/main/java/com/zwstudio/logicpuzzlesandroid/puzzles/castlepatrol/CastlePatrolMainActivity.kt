package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CastlePatrolMainActivity : GameMainActivity<CastlePatrolGame, CastlePatrolDocument, CastlePatrolGameMove, CastlePatrolGameState>() {
    private val document: CastlePatrolDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CastlePatrolOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CastlePatrolGameActivity::class.java))
    }
}

class CastlePatrolOptionsActivity : GameOptionsActivity<CastlePatrolGame, CastlePatrolDocument, CastlePatrolGameMove, CastlePatrolGameState>() {
    private val document: CastlePatrolDocument by inject()
    override val doc get() = document
}

class CastlePatrolHelpActivity : GameHelpActivity<CastlePatrolGame, CastlePatrolDocument, CastlePatrolGameMove, CastlePatrolGameState>() {
    private val document: CastlePatrolDocument by inject()
    override val doc get() = document
}