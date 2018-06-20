package com.kicker.model

import com.kicker.model.base.BaseModel
import javax.persistence.*
import javax.validation.constraints.Max

/**
 * @author Yauheni Efimenko
 */
@Entity
@Table(name = "dashboard_rating")
class DashboardRating(

        @ManyToOne
        @JoinColumn(name = "player_id", nullable = false)
        val player: Player,

        @Column(name = "delta", nullable = false)
        private val delta: Double,

        @Max(WEEKS_RATED.toLong(), message = "This column cannot be more $WEEKS_RATED")
        @Column(name = "week_ago", nullable = false)
        var weekAgo: Int = 0

) : BaseModel() {

    companion object {
        private const val WEEKS_RATED = 10
        private const val OBSOLESCENCE_STEP = 0.1
    }


    fun getDelta(): Double = delta * (WEEKS_RATED - weekAgo) * OBSOLESCENCE_STEP

}