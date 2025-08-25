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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pesaflow.sdk.ui.theme.*
import com.pesaflow.sdk.model.PaymentError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentFailedScreen(
    modifier: Modifier = Modifier,
    paymentError: PaymentError?,
    onRetryClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Error Notification
            PaymentErrorNotification(
                paymentError = paymentError,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Order Summary Card
            FailedOrderSummaryCard(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Error Details Card
            if (paymentError != null) {
                PaymentErrorDetailsCard(
                    paymentError = paymentError,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Retry Button
            Button(
                onClick = onRetryClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Color(0xFFEA9D2C)
                ),
                border = BorderStroke(1.dp, Color.Transparent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Try different Card",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEA9D2C)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Support Section
            FailedSupportSection(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PaymentErrorNotification(
    paymentError: PaymentError?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F0)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 12.dp, 11.dp, 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Warning Icon
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFBC252B),
                modifier = Modifier.size(19.dp)
            )
            
            // Error Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Payment Not Processed",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFBC252B)
                )
                
                Text(
                    text = paymentError?.message ?: "Your payment couldn't be completed.",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFBC252B),
                    lineHeight = 12.sp
                )
            }
        }
    }
}

@Composable
private fun FailedOrderSummaryCard(
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
            // Header with collapse icon
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
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
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
private fun PaymentErrorDetailsCard(
    paymentError: PaymentError,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            // Possible reasons
            if (paymentError.reasons.isNotEmpty()) {
                Text(
                    text = "Possible reasons:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                
                Text(
                    text = paymentError.reasons.joinToString("\n"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
            
            // Suggested actions
            if (paymentError.suggestions.isNotEmpty()) {
                Text(
                    text = "Suggested actions:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                
                Text(
                    text = paymentError.suggestions.joinToString("\n"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun FailedSupportSection(
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