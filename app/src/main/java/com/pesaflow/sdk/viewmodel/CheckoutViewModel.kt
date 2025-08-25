package com.pesaflow.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.pesaflow.sdk.model.PaymentStatus
import com.pesaflow.sdk.model.PaymentError

data class CheckoutFormState(
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val firstName: String = "",
    val otherNames: String = "",
    val emailAddress: String = "",
    val city: String = "",
    val state: String = "",
    val saveCardDetails: Boolean = false
)

data class FormFieldError(
    val cardNumberError: String? = null,
    val expiryDateError: String? = null,
    val cvvError: String? = null,
    val firstNameError: String? = null,
    val emailAddressError: String? = null,
    val cityError: String? = null,
    val stateError: String? = null
)

data class CheckoutUiState(
    val formState: CheckoutFormState = CheckoutFormState(),
    val fieldErrors: FormFieldError = FormFieldError(),
    val isOrderSummaryExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
    val paymentStatus: PaymentStatus = PaymentStatus.FORM_INPUT,
    val paymentError: PaymentError? = null
)

class CheckoutViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()
    
    fun updateCardNumber(cardNumber: String) {
        val formState = _uiState.value.formState.copy(cardNumber = cardNumber)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateExpiryDate(expiryDate: String) {
        val formState = _uiState.value.formState.copy(expiryDate = expiryDate)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateCvv(cvv: String) {
        val formState = _uiState.value.formState.copy(cvv = cvv)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateFirstName(firstName: String) {
        val formState = _uiState.value.formState.copy(firstName = firstName)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateOtherNames(otherNames: String) {
        val formState = _uiState.value.formState.copy(otherNames = otherNames)
        _uiState.value = _uiState.value.copy(
            formState = formState
        )
    }
    
    fun updateEmailAddress(emailAddress: String) {
        val formState = _uiState.value.formState.copy(emailAddress = emailAddress)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateCity(city: String) {
        val formState = _uiState.value.formState.copy(city = city)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateState(state: String) {
        val formState = _uiState.value.formState.copy(state = state)
        val fieldErrors = validateFieldErrors(formState)
        _uiState.value = _uiState.value.copy(
            formState = formState,
            fieldErrors = fieldErrors,
            isFormValid = validateForm(formState)
        )
    }
    
    fun updateSaveCardDetails(saveCardDetails: Boolean) {
        val formState = _uiState.value.formState.copy(saveCardDetails = saveCardDetails)
        _uiState.value = _uiState.value.copy(formState = formState)
    }
    
    fun toggleOrderSummary() {
        _uiState.value = _uiState.value.copy(
            isOrderSummaryExpanded = !_uiState.value.isOrderSummaryExpanded
        )
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun processPayment() {
        if (!_uiState.value.isFormValid) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please fill all required fields")
            return
        }
        
        _uiState.value = _uiState.value.copy(
            isLoading = true, 
            errorMessage = null,
            paymentStatus = PaymentStatus.PROCESSING,
            paymentError = null
        )
        
        // Simulate payment processing
        viewModelScope.launch {
            delay(3000) // Simulate processing delay
            
            // For demo purposes, randomly succeed or fail
            val isSuccess = (0..10).random() > 3 // 70% success rate
            
            if (isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentStatus = PaymentStatus.SUCCESS
                )
            } else {
                val error = PaymentError(
                    message = "Your payment couldn't be completed.",
                    reasons = listOf(
                        "Insufficient funds",
                        "Card limit exceeded", 
                        "Bank security hold"
                    ),
                    suggestions = listOf(
                        "Contact your bank",
                        "Try a different payment method",
                        "Check your account balance"
                    )
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paymentStatus = PaymentStatus.FAILED,
                    paymentError = error
                )
            }
        }
    }
    
    fun retryPayment() {
        _uiState.value = _uiState.value.copy(
            paymentStatus = PaymentStatus.FORM_INPUT,
            paymentError = null,
            errorMessage = null
        )
    }
    
    fun completePayment() {
        // Handle payment completion - typically navigate away or reset
        _uiState.value = _uiState.value.copy(
            paymentStatus = PaymentStatus.FORM_INPUT,
            formState = CheckoutFormState(), // Reset form
            fieldErrors = FormFieldError(),
            paymentError = null,
            errorMessage = null
        )
    }
    
    private fun validateFieldErrors(formState: CheckoutFormState): FormFieldError {
        return FormFieldError(
            cardNumberError = validateCardNumber(formState.cardNumber),
            expiryDateError = validateExpiryDate(formState.expiryDate),
            cvvError = validateCvv(formState.cvv),
            firstNameError = validateFirstName(formState.firstName),
            emailAddressError = validateEmailAddress(formState.emailAddress),
            cityError = validateCity(formState.city),
            stateError = validateState(formState.state)
        )
    }
    
    private fun validateForm(formState: CheckoutFormState): Boolean {
        val errors = validateFieldErrors(formState)
        return errors.cardNumberError == null &&
                errors.expiryDateError == null &&
                errors.cvvError == null &&
                errors.firstNameError == null &&
                errors.emailAddressError == null &&
                errors.cityError == null &&
                errors.stateError == null
    }
    
    private fun validateCardNumber(cardNumber: String): String? {
        return when {
            cardNumber.isBlank() -> null // Don't show error for empty field
            !isValidCardNumber(cardNumber) -> "Card number is invalid"
            else -> null
        }
    }
    
    private fun validateExpiryDate(expiryDate: String): String? {
        return when {
            expiryDate.isBlank() -> null // Don't show error for empty field
            !isValidExpiryDate(expiryDate) -> "Date is invalid"
            else -> null
        }
    }
    
    private fun validateCvv(cvv: String): String? {
        return when {
            cvv.isBlank() -> null // Don't show error for empty field
            !isValidCvv(cvv) -> "The security code for this card is invalid"
            else -> null
        }
    }
    
    private fun validateFirstName(firstName: String): String? {
        return when {
            firstName.isBlank() -> null // Don't show error for empty field
            firstName.length < 2 -> "First name must be at least 2 characters"
            else -> null
        }
    }
    
    private fun validateEmailAddress(emailAddress: String): String? {
        return when {
            emailAddress.isBlank() -> null // Don't show error for empty field
            !isValidEmail(emailAddress) -> "Please enter a valid email address"
            else -> null
        }
    }
    
    private fun validateCity(city: String): String? {
        return when {
            city.isBlank() -> null // Don't show error for empty field
            city.length < 2 -> "City name must be at least 2 characters"
            else -> null
        }
    }
    
    private fun validateState(state: String): String? {
        return when {
            state.isBlank() -> null // Don't show error for empty field
            state.length < 2 -> "State name must be at least 2 characters"
            else -> null
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    private fun isValidCardNumber(cardNumber: String): Boolean {
        // Basic validation - remove spaces and check if it's numeric and has 13-19 digits
        val cleaned = cardNumber.replace(" ", "")
        return cleaned.matches(Regex("^[0-9]{13,19}$"))
    }
    
    private fun isValidExpiryDate(expiryDate: String): Boolean {
        // Basic MM/YY format validation
        return expiryDate.matches(Regex("^(0[1-9]|1[0-2])/[0-9]{2}$"))
    }
    
    private fun isValidCvv(cvv: String): Boolean {
        // Basic CVV validation - 3 or 4 digits
        return cvv.matches(Regex("^[0-9]{3,4}$"))
    }
}