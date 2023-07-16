package com.example.luckycardgame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.model.Participant
import com.example.luckycardgame.model.Table
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun givenParticipantCount_whenMakeCards_thenCorrectCardCount() {
        val participant3 = viewModel.makeCards(3)
        val participant4 = viewModel.makeCards(4)
        val participant5 = viewModel.makeCards(5)

        assertEquals(33, participant3.size)
        assertEquals(36, participant4.size)
        assertEquals(36, participant5.size)
    }

    @Test
    fun givenParticipantCount_whenPickCards_thenCorrectCardCountsForParticipantsAndTable() {
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
    fun givenParticipantsAndTable_whenSortingCards_thenParticipantCardsAndTableCardsAreSorted() {
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
    fun givenParticipants_whenCheckingForThreeOfSameNumber_thenAtLeastOneParticipantHasThreeMatchingCards() {
        val participantCount = 5
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val hasThreeOfSameNumber = participants?.any { participant ->
            participant.hasThreeOfSameNumber()
        } ?: false

        assertTrue(hasThreeOfSameNumber)
    }

    @Test
    fun givenParticipantsWithMatchingCardsAndTableCards_whenCheckingForThreeMatchingCards_thenTrue() {
        // 픽 카드 수를 5로 설정하여 테스트
        viewModel.pickCards(5)

        val participants = viewModel.participantsLiveData.value
        val table = viewModel.tableLiveData.value

        // 1번 참가자와 2번 참가자의 카드 중에서 각각 가장 낮은 숫자를 선택
        val lowestNumberParticipant1 = participants?.get(0)?.retrieveParticipantCards()?.minByOrNull { it.number }?.number ?: -1
        val lowestNumberParticipant2 = participants?.get(1)?.retrieveParticipantCards()?.minByOrNull { it.number }?.number ?: -1

        // 테이블의 카드 중 아무거나 선택
        val randomTableCard = table?.retrieveTableCards()?.randomOrNull()

        // 선택한 숫자와 바닥 카드의 숫자가 모두 같은지 확인
        val hasThreeMatchingCards = lowestNumberParticipant1 == lowestNumberParticipant2 && lowestNumberParticipant1 == randomTableCard?.number

        assertTrue(hasThreeMatchingCards)
    }

    @Test
    fun givenCards_whenCheckingForCardCountsByType_thenNoCardWithNumber12ExistsForEachType() {
        val cards = viewModel.makeCards(3)

        assertFalse(cards.any { it.type == "🐶" && it.number == 12 })
        assertFalse(cards.any { it.type == "🐱" && it.number == 12 })
        assertFalse(cards.any { it.type == "🐮" && it.number == 12 })
    }

    @Test
    fun testSetResultData_AllCardsAreSeven() {
        val owner = "A"
        val cards = listOf(Card("🐶", 7, false), Card("🐱", 7, false), Card("🐮", 7, false)) // cards 변수에 카드 리스트 [Card(7), Card(7), Card(7)] 할당

        viewModel.setResultData(owner, cards)

        val expectedData = mutableListOf(
            mapOf(owner to cards)
        )

        assertEquals(expectedData, viewModel.selectedCards.value)
    }


    @Test
    fun testSetResultData_cardsIsSumAndDifferenceSeven() {
        val ownerA = "A"
        val ownerB = "B"
        val cards1 = listOf(Card("🐶", 2, false), Card("🐱", 4, false), Card("🐮", 6, false))
        val cards2 = listOf(Card("🐶", 5, false), Card("🐱", 3, false), Card("🐮", 1, false))

        viewModel.participans = 2

        viewModel.setResultData(ownerA, cards1)
        viewModel.setResultData(ownerB, cards2)

        val expectedData = mutableListOf(
            mapOf(ownerA to cards1, ownerB to cards2),
        )

        assertEquals(expectedData, viewModel.selectedCards.value)
    }

    @Test
    fun testSetResultData_cardsIsSumAndDifferenceSeven2() {
        val ownerA = "A"
        val ownerB = "B"
        val cards1 = listOf(Card("🐶", 1, false), Card("🐱", 1, false), Card("🐮", 1, false))
        val cards2 = listOf(Card("🐶", 8, false), Card("🐱", 8, false), Card("🐮", 8, false))

        viewModel.participans = 2

        viewModel.setResultData(ownerA, cards1)
        viewModel.setResultData(ownerB, cards2)

        val expectedData = mutableListOf(
            mapOf(ownerA to cards1, ownerB to cards2),
        )

        assertEquals(expectedData, viewModel.selectedCards.value)
    }

    @Test
    fun testSetResultData_cardsIsSumAndDifferenceNotSeven() {
        val ownerA = "A"
        val ownerB = "B"
        val cards1 = listOf(Card("🐶", 1, false), Card("🐱", 1, false), Card("🐮", 1, false))
        val cards2 = listOf(Card("🐶", 2, false), Card("🐱", 3, false), Card("🐮", 2, false))

        viewModel.participans = 2

        viewModel.setResultData(ownerA, cards1)
        viewModel.setResultData(ownerB, cards2)

        val expectedData = mutableListOf<Map<String, List<Card>>>()

        assertEquals(expectedData, viewModel.selectedCards.value)
    }
}

