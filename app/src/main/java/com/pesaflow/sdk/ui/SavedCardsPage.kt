package com.pesaflow.sdk.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pesaflow.sdk.ui.theme.*
import com.pesaflow.sdk.viewmodel.SavedCardsViewModel
import com.pesaflow.sdk.viewmodel.SavedCard
import com.pesaflow.sdk.viewmodel.CardStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCardsPage(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onAddPaymentMethodClick: () -> Unit = {},
    viewModel: SavedCardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Event handlers
    val onCardSelected = { cardId: String ->
        viewModel.selectCard(cardId)
    }
    
    val onCardRemoved = { cardId: String ->
        viewModel.removeCard(cardId)
    }

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
                    color = TextPrimary,
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
        
        // Order Summary
        OrderSummaryCard(
            isExpanded = uiState.isOrderSummaryExpanded,
            onExpandClick = { viewModel.toggleOrderSummary() }
        )
        
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Section Title
            Text(
                text = "Enter Card Details",                
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            
            // Cards List
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    uiState.savedCards.forEach { card ->
                        SavedCardItem(
                            card = card,
                            isSelected = card.isSelected,
                            onCardSelected = { onCardSelected(card.id) },
                            onCardRemoved = { onCardRemoved(card.id) }
                        )
                    }
                    
                    // Add Payment Method
                    AddPaymentMethodItem(
                        onClick = onAddPaymentMethodClick
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Support Section
        SupportSection()
        
        // Action Buttons
        ActionButtons(
            onNextClick = onNextClick,
            onCancelClick = onCancelClick
        )
    }
}


@Composable
private fun OrderSummaryCard(
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5 Year Multiple Entry...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextPrimary
                )
            }
            
            // Collapsible content - only show when expanded
            if (isExpanded) {
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
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(White, CircleShape)
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
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(White, CircleShape)
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
}

@Composable
private fun SavedCardItem(
    card: SavedCard,
    isSelected: Boolean,
    onCardSelected: () -> Unit,
    onCardRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardSelected() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BackgroundGray else White
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            1.dp, 
            if (isSelected) BorderGray else BorderLight
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp, 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card Icon/Circle
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(White, CircleShape)
                            .border(1.dp, BorderGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(PesaflowBlue, CircleShape)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, BorderLight, CircleShape)
                    )
                }
                
                Text(
                    text = "Debit card ending in ${card.cardNumber}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
            
            // Status and Remove
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (card.status == CardStatus.VALID) SuccessGreenLight else ErrorRedLight
                    )
                ) {
                    Text(
                        text = if (card.status == CardStatus.VALID) "Valid 29" else "Expired on 09/2024",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (card.status == CardStatus.VALID) SuccessGreen else ErrorRed,
                        modifier = Modifier.padding(2.dp, 8.dp)
                    )
                }
                
                Text(
                    text = "Remove",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red,
                    letterSpacing = 0.12.sp,
                    modifier = Modifier.clickable { onCardRemoved() }
                )
            }
        }
    }
}

@Composable
private fun AddPaymentMethodItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .drawBehind {
                drawRoundRect(
                    color = PesaflowBlue,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                )
            }
            .background(BackgroundGray, RoundedCornerShape(12.dp))
            .padding(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = PesaflowBlue,
                modifier = Modifier.size(16.dp)
            )
            
            Text(
                text = "Add a payment method",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PesaflowBlue,
                letterSpacing = 0.14.sp
            )
        }
    }
}

@Composable
private fun SupportSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth(),
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
        
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
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
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(11.dp),
        horizontalArrangement = Arrangement.spacedBy(58.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )
        
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )
        
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SavedCardsPagePreview() {
    PesaflowsdkTheme {
        SavedCardsPage()
    }
}