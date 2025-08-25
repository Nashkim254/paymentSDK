package com.pesaflow.sdk.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SavedCard(
    val id: String,
    val cardNumber: String,
    val status: CardStatus,
    val isSelected: Boolean = false
)

enum class CardStatus {
    VALID,
    EXPIRED
}

data class SavedCardsUiState(
    val savedCards: List<SavedCard> = emptyList(),
    val isOrderSummaryExpanded: Boolean = false,
    val selectedCardId: String? = null
)

class SavedCardsViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        SavedCardsUiState(
            savedCards = listOf(
                SavedCard("1", "**** 8890", CardStatus.VALID, true),
                SavedCard("2", "**** 8890", CardStatus.EXPIRED),
                SavedCard("3", "**** 8890", CardStatus.VALID)
            ),
            selectedCardId = "1"
        )
    )
    val uiState: StateFlow<SavedCardsUiState> = _uiState.asStateFlow()
    
    fun toggleOrderSummary() {
        _uiState.value = _uiState.value.copy(
            isOrderSummaryExpanded = !_uiState.value.isOrderSummaryExpanded
        )
    }
    
    fun selectCard(cardId: String) {
        val updatedCards = _uiState.value.savedCards.map { card ->
            card.copy(isSelected = card.id == cardId)
        }
        
        _uiState.value = _uiState.value.copy(
            savedCards = updatedCards,
            selectedCardId = cardId
        )
    }
    
    fun removeCard(cardId: String) {
        val updatedCards = _uiState.value.savedCards.filter { it.id != cardId }
        val newSelectedCardId = if (_uiState.value.selectedCardId == cardId) {
            updatedCards.firstOrNull()?.id
        } else {
            _uiState.value.selectedCardId
        }
        
        _uiState.value = _uiState.value.copy(
            savedCards = updatedCards,
            selectedCardId = newSelectedCardId
        )
    }
    
    fun getSelectedCard(): SavedCard? {
        return _uiState.value.savedCards.find { it.id == _uiState.value.selectedCardId }
    }
}