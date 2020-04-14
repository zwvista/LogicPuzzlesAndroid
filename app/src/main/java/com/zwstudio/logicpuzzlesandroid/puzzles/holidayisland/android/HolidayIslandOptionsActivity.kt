package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.data.HolidayIslandDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class HolidayIslandOptionsActivity : GameOptionsActivity<HolidayIslandGame?, HolidayIslandDocument?, HolidayIslandGameMove?, HolidayIslandGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: HolidayIslandDocument? = null
    override fun doc() = document!!
}