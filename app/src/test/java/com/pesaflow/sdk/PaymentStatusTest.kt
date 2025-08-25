package com.pesaflow.sdk

import com.pesaflow.sdk.model.PaymentStatus
import com.pesaflow.sdk.model.PaymentError
import com.pesaflow.sdk.viewmodel.CheckoutViewModel
import org.junit.Test
import org.junit.Assert.*

class PaymentStatusTest {
    
    @Test
    fun `initial state should be FORM_INPUT`() {
        val viewModel = CheckoutViewModel()
        val initialState = viewModel.uiState.value
        
        assertEquals(PaymentStatus.FORM_INPUT, initialState.paymentStatus)
        assertNull(initialState.paymentError)
        assertFalse(initialState.isLoading)
    }
    
    @Test
    fun `payment status enum values are correct`() {
        val values = PaymentStatus.values()
        
        assertEquals(4, values.size)
        assertTrue(values.contains(PaymentStatus.FORM_INPUT))
        assertTrue(values.contains(PaymentStatus.PROCESSING))
        assertTrue(values.contains(PaymentStatus.SUCCESS))
        assertTrue(values.contains(PaymentStatus.FAILED))
    }
    
    @Test
    fun `payment error contains expected fields`() {
        val error = PaymentError(
            message = "Payment failed",
            reasons = listOf("Insufficient funds", "Card expired"),
            suggestions = listOf("Contact bank", "Use different card")
        )
        
        assertEquals("Payment failed", error.message)
        assertEquals(2, error.reasons.size)
        assertEquals(2, error.suggestions.size)
        assertEquals("Insufficient funds", error.reasons[0])
        assertEquals("Contact bank", error.suggestions[0])
    }
    
    @Test 
    fun `retryPayment resets to FORM_INPUT state`() {
        val viewModel = CheckoutViewModel()
        
        // First, set some failed state
        viewModel.retryPayment()
        
        val state = viewModel.uiState.value
        assertEquals(PaymentStatus.FORM_INPUT, state.paymentStatus)
        assertNull(state.paymentError)
        assertNull(state.errorMessage)
    }
    
    @Test
    fun `completePayment resets to FORM_INPUT and clears form`() {
        val viewModel = CheckoutViewModel()
        
        // Set some form data first
        viewModel.updateCardNumber("1234567890123456")
        viewModel.updateFirstName("John")
        
        // Complete payment
        viewModel.completePayment()
        
        val state = viewModel.uiState.value
        assertEquals(PaymentStatus.FORM_INPUT, state.paymentStatus)
        assertEquals("", state.formState.cardNumber)
        assertEquals("", state.formState.firstName)
        assertNull(state.paymentError)
        assertNull(state.errorMessage)
    }
}