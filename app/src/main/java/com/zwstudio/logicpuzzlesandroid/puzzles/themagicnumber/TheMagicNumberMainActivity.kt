package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class TheMagicNumberMainActivity : GameMainActivity<TheMagicNumberGame, TheMagicNumberDocument, TheMagicNumberGameMove, TheMagicNumberGameState>() {
    private val document: TheMagicNumberDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, TheMagicNumberOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, TheMagicNumberGameActivity::class.java))
    }
}

class TheMagicNumberOptionsActivity : GameOptionsActivity<TheMagicNumberGame, TheMagicNumberDocument, TheMagicNumberGameMove, TheMagicNumberGameState>() {
    private val document: TheMagicNumberDocument by inject()
    override val doc get() = document
}

class TheMagicNumberHelpActivity : GameHelpActivity<TheMagicNumberGame, TheMagicNumberDocument, TheMagicNumberGameMove, TheMagicNumberGameState>() {
    private val document: TheMagicNumberDocument by inject()
    override val doc get() = document
}