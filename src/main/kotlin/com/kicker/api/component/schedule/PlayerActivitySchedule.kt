package com.kicker.api.component.schedule

import com.kicker.api.config.property.PlayerSettingsProperties
import com.kicker.api.service.GameService
import com.kicker.api.service.PlayerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * @author Yauheni Efimenko
 */

@Component
class PlayerActivitySchedule(
        private val gameService: GameService,
        private val playerService: PlayerService,
        private val playerSettingsProperties: PlayerSettingsProperties
) {

    @Scheduled(cron = "0 10 0 * * MON")
    @Transactional
    fun schedulePlayerActivity() {
        playerService.getAll().forEach {
            if (it.active && gameService.countDuring10WeeksByPlayer(it.id) < playerSettingsProperties.countGames!!) {
                playerService.updateActivity(it.id, !it.active)
            }
        }
    }

}