package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.android.MathraxGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.android.MathraxOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.data.MathraxDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MathraxMainActivity : GameMainActivity<MathraxGame?, MathraxDocument?, MathraxGameMove?, MathraxGameState?>() {
    @Bean
    protected var document: MathraxDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        MathraxOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MathraxGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MathraxOptionsActivity : GameOptionsActivity<MathraxGame?, MathraxDocument?, MathraxGameMove?, MathraxGameState?>() {
    @Bean
    protected var document: MathraxDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class MathraxHelpActivity : GameHelpActivity<MathraxGame?, MathraxDocument?, MathraxGameMove?, MathraxGameState?>() {
    @Bean
    protected var document: MathraxDocument? = null
    override fun doc() = document
}