package noah.moohvie.models

data class TrophyMilestone(
    val tier: TrophyTier,
    val threshold: Int,
    /** Epoch millis (équivalent de `Date` côté iOS). */
    val unlockedDate: Long?,
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
