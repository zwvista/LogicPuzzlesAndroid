package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ParkingLotMainActivity : GameMainActivity<ParkingLotGame, ParkingLotDocument, ParkingLotGameMove, ParkingLotGameState>() {
    private val document: ParkingLotDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ParkingLotOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ParkingLotGameActivity::class.java))
    }
}

class ParkingLotOptionsActivity : GameOptionsActivity<ParkingLotGame, ParkingLotDocument, ParkingLotGameMove, ParkingLotGameState>() {
    private val document: ParkingLotDocument by inject()
    override val doc get() = document
}

class ParkingLotHelpActivity : GameHelpActivity<ParkingLotGame, ParkingLotDocument, ParkingLotGameMove, ParkingLotGameState>() {
    private val document: ParkingLotDocument by inject()
    override val doc get() = document
}
