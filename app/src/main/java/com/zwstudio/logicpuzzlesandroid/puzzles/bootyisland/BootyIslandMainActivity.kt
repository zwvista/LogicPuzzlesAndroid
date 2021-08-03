package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BootyIslandMainActivity : GameMainActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    private val document: BootyIslandDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BootyIslandOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BootyIslandGameActivity::class.java))
    }
}

class BootyIslandOptionsActivity : GameOptionsActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    private val document: BootyIslandDocument by inject()
    override val doc get() = document
}

class BootyIslandHelpActivity : GameHelpActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState>() {
    private val document: BootyIslandDocument by inject()
    override val doc get() = document
}
