package com.kicker.api.model

import com.kicker.api.model.base.BaseModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author Yauheni Efimenko
 */
@Entity
@Table(name = "players")
class Player(

        @Column(name = "username", nullable = false, unique = true)
        private var username: String,

        @Column(name = "password", nullable = false)
        private var password: String,

        @Column(name = "rating", nullable = false)
        var rating: Double = PLAYER_RATING,

        @Column(name = "active", nullable = false)
        var active: Boolean = false,

        @Column(name = "icon_name")
        var iconName: String? = null

) : BaseModel(), UserDetails {

    @Column(name = "current_winning_streak", nullable = false)
    var currentWinningStreak: Int = 0
        private set

    @Column(name = "current_losses_streak", nullable = false)
    var currentLossesStreak: Int = 0
        private set

    @Column(name = "longest_winning_streak", nullable = false)
    var longestWinningStreak: Int = 0
        private set

    @Column(name = "longest_losses_streak", nullable = false)
    var longestLossesStreak: Int = 0
        private set

    companion object {
        const val PLAYER_RATING: Double = 10000.0
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    fun setUsername(username: String) {
        this.username = username
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun changeWinAndLossStreak(won: Boolean) {
        if (won) {
            currentWinningStreak++
            currentLossesStreak = 0
            if (longestWinningStreak < currentWinningStreak) {
                longestWinningStreak = currentWinningStreak
            }
        } else {
            currentLossesStreak++
            currentWinningStreak = 0
            if (longestLossesStreak < currentLossesStreak) {
                longestLossesStreak = currentLossesStreak
            }
        }
    }

}