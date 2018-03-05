package rocks.che.elections.helpers

import rocks.che.elections.logic.Gamestate

data class BuyGroupPointsEvent(val group: String, val amount: Int, val totalSpent: Int)
class NextDebateStage
data class DebateTimeDistributionUpdate(val groupMinutes: Int, val opponentMinutes: Int)
data class SetGroupDistribution(val vals: List<Int>)
data class SetOpponentDistribution(val vals: List<Int>)
data class GamestateUpdate(val g: Gamestate)
class ChangeSpending
data class ChooseGroup(val group: String)