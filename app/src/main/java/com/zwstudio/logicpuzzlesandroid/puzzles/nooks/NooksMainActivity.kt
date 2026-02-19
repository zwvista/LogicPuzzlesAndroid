package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class NooksMainActivity : GameMainActivity<NooksGame, NooksDocument, NooksGameMove, NooksGameState>() {
    private val document: NooksDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NooksOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NooksGameActivity::class.java))
    }
}

class NooksOptionsActivity : GameOptionsActivity<NooksGame, NooksDocument, NooksGameMove, NooksGameState>() {
    private val document: NooksDocument by inject()
    override val doc get() = document
}

class NooksHelpActivity : GameHelpActivity<NooksGame, NooksDocument, NooksGameMove, NooksGameState>() {
    private val document: NooksDocument by inject()
    override val doc get() = document
}