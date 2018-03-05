package rocks.che.elections.helpers

data class BuyGroupPointsEvent(val group: String, val amount: Int, val totalSpent: Int)
interface NextDebateStage
val nextDebateStage = object : NextDebateStage {}
data class DebateTimeDistributionUpdate(val groupMinutes: Int, val opponentMinutes: Int)
data class SetGroupDistribution(val vals: List<Int>)
data class SetOpponentDistribution(val vals: List<Int>)
