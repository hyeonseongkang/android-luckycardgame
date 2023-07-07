package com.example.luckycardgame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.model.Participant
import com.example.luckycardgame.model.Table
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LuckyBoardGameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var participantsObserver: Observer<List<Participant>>

    @Mock
    private lateinit var tableObserver: Observer<Table>

    private lateinit var viewModel: LuckyBoardGameViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LuckyBoardGameViewModel()
        viewModel.participantsLiveData.observeForever(participantsObserver)
        viewModel.tableLiveData.observeForever(tableObserver)
    }

    @Test
    fun testMakeCards() {
        val participant3 = viewModel.makeCards(3)
        val participant4 = viewModel.makeCards(4)
        val participant5 = viewModel.makeCards(5)

        assertEquals(33, participant3.size)
        assertEquals(36, participant4.size)
        assertEquals(36, participant5.size)
    }

    @Test
    fun testPickCards() {
        val participantCount = 3
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val table = viewModel.tableLiveData.value

        assertEquals(8, participants?.get(0)?.retrieveParticipantCards()?.size)
        assertEquals(8, participants?.get(1)?.retrieveParticipantCards()?.size)
        assertEquals(8, participants?.get(2)?.retrieveParticipantCards()?.size)
        assertEquals(9, table?.retrieveTableCards()?.size)

    }

    @Test
    fun testSortedCards() {
        val participantCount = 3
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val table = viewModel.tableLiveData.value
        table?.sortCardsByNumber()

        val participantA = participants?.get(0)
        participantA?.sortCardsByNumber()

        val sortedCards = participantA?.retrieveParticipantCards()
        val isParticipantCardsSorted = sortedCards?.zipWithNext()?.all { (card1, card2) ->
            card1.number <= card2.number
        } ?: false

        val tableCards = table?.retrieveTableCards()
        val isTableCardsSorted = tableCards?.zipWithNext()?.all { (card1, card2) ->
            card1.number <= card2.number
        } ?: false

        assertTrue(isParticipantCardsSorted)
        assertTrue(isTableCardsSorted)
    }

    @Test
    fun testHasThreeOfSameNumber() {
        val participantCount = 5
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val hasThreeOfSameNumber = participants?.any { participant ->
            participant.hasThreeOfSameNumber()
        } ?: false

        assertTrue(hasThreeOfSameNumber)
    }

    @Test
    fun testHasThreeOfSameNumberForLowestCard() {
        val participantCount = 3
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val participantA = participants?.get(0)

        // 특정 참가자의 카드 중 가장 낮은 숫자 선택
        val lowestNumber = participantA?.retrieveParticipantCards()?.minByOrNull { it.number }?.number

        // 선택한 숫자와 같은 숫자를 가진 카드 개수 확인
        val countOfSameNumberCards = participantA?.retrieveParticipantCards()?.count { it.number == lowestNumber }

        val hasThreeOfSameNumber = countOfSameNumberCards == 3
        assertTrue(hasThreeOfSameNumber)
    }

    @Test
    fun testCardCountsByType() {
        val cards = viewModel.makeCards(5)

        val typeCounts = mutableMapOf<String, Int>()
        for (card in cards) {
            val count = typeCounts.getOrDefault(card.type, 0)
            typeCounts[card.type] = count + 1
        }

        assertEquals(12, typeCounts["🐶"])
        assertEquals(12, typeCounts["🐱"])
        assertEquals(12, typeCounts["🐮"])
    }

}
