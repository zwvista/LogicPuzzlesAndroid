package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class FussyWaiterMainActivity : GameMainActivity<FussyWaiterGame, FussyWaiterDocument, FussyWaiterGameMove, FussyWaiterGameState>() {
    private val document: FussyWaiterDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, FussyWaiterOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, FussyWaiterGameActivity::class.java))
    }
}

class FussyWaiterOptionsActivity : GameOptionsActivity<FussyWaiterGame, FussyWaiterDocument, FussyWaiterGameMove, FussyWaiterGameState>() {
    private val document: FussyWaiterDocument by inject()
    override val doc get() = document
}

class FussyWaiterHelpActivity : GameHelpActivity<FussyWaiterGame, FussyWaiterDocument, FussyWaiterGameMove, FussyWaiterGameState>() {
    private val document: FussyWaiterDocument by inject()
    override val doc get() = document
}