package com.pesaflow.sdk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pesaflow.sdk.ui.CheckoutPage
import com.pesaflow.sdk.ui.SavedCardsPage

object PesaflowDestinations {
    const val SAVED_CARDS_ROUTE = "saved_cards"
    const val CHECKOUT_ROUTE = "checkout"
}

@Composable
fun PesaflowNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PesaflowDestinations.SAVED_CARDS_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(PesaflowDestinations.SAVED_CARDS_ROUTE) {
            SavedCardsPage(
                onNextClick = {
                    // Handle next action if needed
                },
                onCancelClick = {
                    // Handle cancel action if needed
                },
                onBackClick = {
                    // Handle back navigation
                },
                onAddPaymentMethodClick = {
                    navController.navigate(PesaflowDestinations.CHECKOUT_ROUTE)
                }
            )
        }
        
        composable(PesaflowDestinations.CHECKOUT_ROUTE) {
            CheckoutPage(
                onNextClick = {
                    // Handle next action - could navigate to next screen
                },
                onCancelClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}