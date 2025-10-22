package com.zwstudio.logicpuzzlesandroid.puzzles.coffeeandsugar

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CoffeeAndSugarMainActivity : GameMainActivity<CoffeeAndSugarGame, CoffeeAndSugarDocument, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>() {
    private val document: CoffeeAndSugarDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CoffeeAndSugarOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CoffeeAndSugarGameActivity::class.java))
    }
}

class CoffeeAndSugarOptionsActivity : GameOptionsActivity<CoffeeAndSugarGame, CoffeeAndSugarDocument, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>() {
    private val document: CoffeeAndSugarDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CoffeeAndSugarHelpActivity : GameHelpActivity<CoffeeAndSugarGame, CoffeeAndSugarDocument, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>() {
    private val document: CoffeeAndSugarDocument by inject()
    override val doc get() = document
}