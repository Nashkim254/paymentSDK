package com.pesaflow.sdk.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pesaflow.sdk.ui.theme.*
import com.pesaflow.sdk.viewmodel.CheckoutViewModel
import com.pesaflow.sdk.model.PaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: CheckoutViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Handle different payment states
    when (uiState.paymentStatus) {
        PaymentStatus.FORM_INPUT -> {
            CheckoutFormScreen(
                modifier = modifier,
                uiState = uiState,
                onNextClick = { viewModel.processPayment() },
                onCancelClick = onCancelClick,
                onBackClick = onBackClick,
                onFieldUpdate = { field, value ->
                    when (field) {
                        "cardNumber" -> viewModel.updateCardNumber(value)
                        "expiryDate" -> viewModel.updateExpiryDate(value)
                        "cvv" -> viewModel.updateCvv(value)
                        "firstName" -> viewModel.updateFirstName(value)
                        "otherNames" -> viewModel.updateOtherNames(value)
                        "emailAddress" -> viewModel.updateEmailAddress(value)
                        "city" -> viewModel.updateCity(value)
                        "state" -> viewModel.updateState(value)
                    }
                },
                onSaveCardDetailsUpdate = viewModel::updateSaveCardDetails,
                onToggleOrderSummary = viewModel::toggleOrderSummary
            )
        }
        PaymentStatus.PROCESSING -> {
            ProcessingPaymentScreen(
                modifier = modifier,
                onCancelClick = onCancelClick,
                onBackClick = onBackClick
            )
        }
        PaymentStatus.SUCCESS -> {
            PaymentSuccessScreen(
                modifier = modifier,
                onCompleteClick = { 
                    viewModel.completePayment()
                    onNextClick()
                },
                onBackClick = onBackClick
            )
        }
        PaymentStatus.FAILED -> {
            PaymentFailedScreen(
                modifier = modifier,
                paymentError = uiState.paymentError,
                onRetryClick = { viewModel.retryPayment() },
                onBackClick = onBackClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutFormScreen(
    modifier: Modifier = Modifier,
    uiState: com.pesaflow.sdk.viewmodel.CheckoutUiState,
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
    onFieldUpdate: (String, String) -> Unit,
    onSaveCardDetailsUpdate: (Boolean) -> Unit,
    onToggleOrderSummary: () -> Unit
) {
    val formState = uiState.formState
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // App Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Checkout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back",
                        tint = TextSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Payment Verification Notice
            PaymentVerificationNotice(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Order Summary Card
            OrderSummaryCard(
                isExpanded = uiState.isOrderSummaryExpanded,
                onExpandClick = onToggleOrderSummary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Card Details Section
            Text(
                text = "Enter Card Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            
            // Card Details Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Number
                    ValidatedTextField(
                        value = formState.cardNumber,
                        onValueChange = { onFieldUpdate("cardNumber", it) },
                        label = "Card number",
                        placeholder = "|",
                        errorMessage = uiState.fieldErrors.cardNumberError,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = "Credit Card",
                                tint = if (uiState.fieldErrors.cardNumberError != null) Color(0xFFEF1E27) else BorderGray
                            )
                        }
                    )
                    
                    // Expiry Date
                    ValidatedTextField(
                        value = formState.expiryDate,
                        onValueChange = { onFieldUpdate("expiryDate", it) },
                        label = "Expiry date",
                        placeholder = "MM/YY",
                        errorMessage = uiState.fieldErrors.expiryDateError
                    )
                    
                    // CVC/CVV
                    ValidatedTextField(
                        value = formState.cvv,
                        onValueChange = { onFieldUpdate("cvv", it) },
                        label = "CVC/CVV",
                        placeholder = "...",
                        errorMessage = uiState.fieldErrors.cvvError,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Security",
                                tint = if (uiState.fieldErrors.cvvError != null) Color(0xFFFF3434) else BorderGray
                            )
                        }
                    )
                    
                    // First Name
                    ValidatedTextField(
                        value = formState.firstName,
                        onValueChange = { onFieldUpdate("firstName", it) },
                        label = "First Name",
                        errorMessage = uiState.fieldErrors.firstNameError
                    )
                    
                    // Other Name(s)
                    ValidatedTextField(
                        value = formState.otherNames,
                        onValueChange = { onFieldUpdate("otherNames", it) },
                        label = "Other Name(s)"
                    )
                    
                    // Email Address
                    ValidatedTextField(
                        value = formState.emailAddress,
                        onValueChange = { onFieldUpdate("emailAddress", it) },
                        label = "Email Address",
                        errorMessage = uiState.fieldErrors.emailAddressError
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // City
                        ValidatedTextField(
                            value = formState.city,
                            onValueChange = { onFieldUpdate("city", it) },
                            label = "City",
                            placeholder = "Type",
                            errorMessage = uiState.fieldErrors.cityError,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // State
                        ValidatedTextField(
                            value = formState.state,
                            onValueChange = { onFieldUpdate("state", it) },
                            label = "State",
                            placeholder = "Type",
                            errorMessage = uiState.fieldErrors.stateError,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Save Card Details Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = formState.saveCardDetails,
                            onCheckedChange = onSaveCardDetailsUpdate,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = PesaflowBlue,
                                uncheckedThumbColor = White,
                                uncheckedTrackColor = BorderGray
                            )
                        )
                        Text(
                            text = "Save this card details.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Support Section
            SupportSection(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            ActionButtons(
                onNextClick = onNextClick,
                onCancelClick = onCancelClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PaymentVerificationNotice(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEFCB)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFEE6D39),
                modifier = Modifier.size(15.dp)
            )
            
            Text(
                text = "Payment Verification Notice: To ensure secure processing, your transaction may require additional verification. Depending on your card type and issuer, this may include authentication steps such as 3D Secure or other verification methods. Please follow the prompts provided during checkout.",
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun OrderSummaryCard(
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier.padding(14.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Collapsible Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5 Year Multiple Entry...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                IconButton(onClick = onExpandClick) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = TextPrimary
                    )
                }
            }
            
            // Total Payable
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Payable",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Text(
                    text = "$170.00",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PesaflowBlue
                )
            }
            
            // Status and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Badge
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarningOrangeLight),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Not paid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = WarningOrange,
                            modifier = Modifier.padding(8.dp, 4.dp)
                        )
                    }
                    
                    // Action Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = TextPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Download",
                                tint = TextPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Pesaflow Logo placeholder
            Box(
                modifier = Modifier
                    .width(97.dp)
                    .height(12.dp)
                    .background(Color.Gray, RoundedCornerShape(2.dp))
            )
            
            // Contact Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "support@pesaflow.com",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "+254 207 903 260",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary
            ),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Button(
            onClick = onNextClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = PesaflowBlue
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Next",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
        }
    }
}

@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            trailingIcon = trailingIcon,
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorMessage != null) Color(0xFFFF3434) else PesaflowBlue,
                unfocusedBorderColor = if (errorMessage != null) Color(0xFFFF3434) else Color(0xFFD6DEFF),
                errorBorderColor = Color(0xFFFF3434)
            )
        )
        
        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF0909),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutPagePreview() {
    PesaflowsdkTheme {
        CheckoutPage()
    }
}