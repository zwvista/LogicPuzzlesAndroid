package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HitoriMainActivity : GameMainActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    private val document: HitoriDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HitoriOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HitoriGameActivity::class.java))
    }
}

class HitoriOptionsActivity : GameOptionsActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    private val document: HitoriDocument by inject()
    override val doc get() = document
}

class HitoriHelpActivity : GameHelpActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    private val document: HitoriDocument by inject()
    override val doc get() = document
}
