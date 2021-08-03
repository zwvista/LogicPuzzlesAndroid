package com.zwstudio.logicpuzzlesandroid.puzzles.orchards

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class OrchardsMainActivity : GameMainActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState>() {
    private val document: OrchardsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, OrchardsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, OrchardsGameActivity::class.java))
    }
}

class OrchardsOptionsActivity : GameOptionsActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState>() {
    private val document: OrchardsDocument by inject()
    override val doc get() = document
}

class OrchardsHelpActivity : GameHelpActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState>() {
    private val document: OrchardsDocument by inject()
    override val doc get() = document
}