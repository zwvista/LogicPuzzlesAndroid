package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android.MineShipsGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.android.MineShipsOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data.MineShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MineShipsMainActivity : GameMainActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        MineShipsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MineShipsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MineShipsOptionsActivity : GameOptionsActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class MineShipsHelpActivity : GameHelpActivity<MineShipsGame, MineShipsDocument, MineShipsGameMove, MineShipsGameState>() {
    @Bean
    protected lateinit var document: MineShipsDocument
    override fun doc() = document
}