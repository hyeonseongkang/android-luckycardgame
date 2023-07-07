package com.example.luckycardgame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer
import com.example.luckycardgame.model.Participant
import com.example.luckycardgame.model.Table
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
        val cards = viewModel.makeCards(4)
        assertEquals(36, cards.size)
    }

    @Test
    fun testPickCards() {
        val participantCount = 4
        viewModel.pickCards(participantCount)

        val participants = viewModel.participantsLiveData.value
        val table = viewModel.tableLiveData.value

        assertEquals(participantCount, participants?.size)
        assertEquals(7, participants?.get(0)?.retrieveParticipantCards()?.size)
        assertEquals(7, participants?.get(1)?.retrieveParticipantCards()?.size)
        assertEquals(7, participants?.get(2)?.retrieveParticipantCards()?.size)
        assertEquals(7, participants?.get(3)?.retrieveParticipantCards()?.size)
        assertEquals(8, table?.retrieveTableCards()?.size)

    }

}
