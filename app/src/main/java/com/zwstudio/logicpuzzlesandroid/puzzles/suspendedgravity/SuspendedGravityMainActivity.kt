package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SuspendedGravityMainActivity : GameMainActivity<SuspendedGravityGame, SuspendedGravityDocument, SuspendedGravityGameMove, SuspendedGravityGameState>() {
    private val document: SuspendedGravityDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SuspendedGravityOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SuspendedGravityGameActivity::class.java))
    }
}

class SuspendedGravityOptionsActivity : GameOptionsActivity<SuspendedGravityGame, SuspendedGravityDocument, SuspendedGravityGameMove, SuspendedGravityGameState>() {
    private val document: SuspendedGravityDocument by inject()
    override val doc get() = document
}

class SuspendedGravityHelpActivity : GameHelpActivity<SuspendedGravityGame, SuspendedGravityDocument, SuspendedGravityGameMove, SuspendedGravityGameState>() {
    private val document: SuspendedGravityDocument by inject()
    override val doc get() = document
}