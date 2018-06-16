package com.kicker.model

import com.kicker.model.base.BaseModel
import com.kicker.model.dictionary.AwardDegree
import com.kicker.model.dictionary.AwardType
import com.kicker.utils.DictionaryUtils
import javax.persistence.*

/**
 * @author Yauheni Efimenko
 */
@Entity
@Table(name = "awards")
class Award(

        @ManyToOne
        @JoinColumn(name = "player_id", nullable = false)
        val player: Player,

        @Column(name = "award_type_id", nullable = false)
        private val awardTypeId: Int,

        @Column(name = "award_degree_id", nullable = false)
        private val awardDegreeId: Int,

        @Column(name = "description", nullable = false)
        val description: String

) : BaseModel() {

    fun getAwardType(): AwardType = DictionaryUtils.valueOf(AwardType::class.java, awardTypeId)

    fun getAwardDegree(): AwardDegree = DictionaryUtils.valueOf(AwardDegree::class.java, awardDegreeId)

}