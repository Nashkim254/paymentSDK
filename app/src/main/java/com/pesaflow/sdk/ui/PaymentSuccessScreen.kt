package com.pesaflow.sdk.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onCompleteClick: () -> Unit = {},
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
            Spacer(modifier = Modifier.height(32.dp))
            
            // Success Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color(0xFF10B981).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Success Text
            Text(
                text = "Transaction successful",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF5E5E5E)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Order Summary Card
            SuccessOrderSummaryCard(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Complete Button
            Button(
                onClick = onCompleteClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(119.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEA9D2C)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Complete",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Support Section
            SuccessSupportSection(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SuccessOrderSummaryCard(
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
                    // Success Status Badge
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF3)),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Paid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF027A48),
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
private fun SuccessSupportSection(
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