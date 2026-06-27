package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ProofOfQuiltMainActivity : GameMainActivity<ProofOfQuiltGame, ProofOfQuiltDocument, ProofOfQuiltGameMove, ProofOfQuiltGameState>() {
    private val document: ProofOfQuiltDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ProofOfQuiltOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ProofOfQuiltGameActivity::class.java))
    }
}

class ProofOfQuiltOptionsActivity : GameOptionsActivity<ProofOfQuiltGame, ProofOfQuiltDocument, ProofOfQuiltGameMove, ProofOfQuiltGameState>() {
    private val document: ProofOfQuiltDocument by inject()
    override val doc get() = document
}

class ProofOfQuiltHelpActivity : GameHelpActivity<ProofOfQuiltGame, ProofOfQuiltDocument, ProofOfQuiltGameMove, ProofOfQuiltGameState>() {
    private val document: ProofOfQuiltDocument by inject()
    override val doc get() = document
}
