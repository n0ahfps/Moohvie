package noah.moohvie.models

import java.util.Date

data class TrophyMilestone(
    val tier: TrophyTier,
    val threshold: Int,
    val unlockedDate: Date?,
) {
    val isUnlocked: Boolean get() = unlockedDate != null
}

data class TrophyProgress(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val count: Int,
    val tier: TrophyTier?,
    val nextThreshold: Int?,
    val milestones: List<TrophyMilestone>,
) {
    val isUnlocked: Boolean get() = tier != null
}
