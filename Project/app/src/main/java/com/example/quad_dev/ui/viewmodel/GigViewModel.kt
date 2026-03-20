package com.example.quad_dev.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class RiskLevel(val label: String, val premium: Int) {
    LOW("LOW", 20),
    MEDIUM("MEDIUM", 50),
    HIGH("HIGH", 80)
}

data class UserData(
    val name: String = "",
    val city: String = "",
    val dailyIncome: String = "",
    val riskLevel: RiskLevel = RiskLevel.LOW
)

sealed class SimulationStep {
    object Idle : SimulationStep()
    object Analyzing : SimulationStep()
    object EventDetected : SimulationStep()
    data class PayoutTriggered(val amount: Double, val isVerified: Boolean) : SimulationStep()
}

class GigViewModel : ViewModel() {
    private val _userOnboarded = MutableStateFlow(false)
    val userOnboarded: StateFlow<Boolean> = _userOnboarded.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    // Simulation State
    private val _currentStep = MutableStateFlow<SimulationStep>(SimulationStep.Idle)
    val currentStep: StateFlow<SimulationStep> = _currentStep.asStateFlow()

    fun updateUserData(name: String, city: String, dailyIncome: String) {
        val calculatedRisk = when (city.trim().lowercase()) {
            "delhi" -> RiskLevel.HIGH
            "mumbai" -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
        
        _userData.value = UserData(
            name = name,
            city = city,
            dailyIncome = dailyIncome,
            riskLevel = calculatedRisk
        )
    }

    fun completeOnboarding() {
        _userOnboarded.value = true
    }

    fun runSimulation() {
        viewModelScope.launch {
            _currentStep.value = SimulationStep.Analyzing
            delay(2000)
            
            _currentStep.value = SimulationStep.EventDetected
            delay(1500)
            
            val income = _userData.value.dailyIncome.toDoubleOrNull() ?: 800.0
            val payout = (income / 8) * 3
            
            // Simulation logic for fraud check: 80% chance of verification
            val isVerified = Random.nextFloat() > 0.2f
            
            _currentStep.value = SimulationStep.PayoutTriggered(payout, isVerified)
        }
    }
    
    fun resetSimulation() {
        _currentStep.value = SimulationStep.Idle
    }
}
