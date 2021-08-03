package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LightenUpMainActivity : GameMainActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    private val document: LightenUpDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LightenUpOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LightenUpGameActivity::class.java))
    }
}

class LightenUpOptionsActivity : GameOptionsActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    private val document: LightenUpDocument by inject()
    override val doc get() = document
}

class LightenUpHelpActivity : GameHelpActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    private val document: LightenUpDocument by inject()
    override val doc get() = document
}