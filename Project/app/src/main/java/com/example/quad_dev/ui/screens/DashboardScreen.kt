package com.example.quad_dev.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quad_dev.ui.viewmodel.GigViewModel
import com.example.quad_dev.ui.viewmodel.RiskLevel

@Composable
fun DashboardScreen(viewModel: GigViewModel) {
    val userData by viewModel.userData.collectAsState()
    val scrollState = rememberScrollState()

    val riskColor = when (userData.riskLevel) {
        RiskLevel.HIGH -> Color(0xFFD32F2F) // Red
        RiskLevel.MEDIUM -> Color(0xFFFBC02D) // Yellow
        RiskLevel.LOW -> Color(0xFF388E3C) // Green
    }
    
    val riskBgColor = when (userData.riskLevel) {
        RiskLevel.HIGH -> Color(0xFFFFEBEE)
        RiskLevel.MEDIUM -> Color(0xFFFFFDE7)
        RiskLevel.LOW -> Color(0xFFE8F5E9)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        // Fintech Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello, ${userData.name.ifEmpty { "Worker" }} 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Your income is protected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Risk Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = riskBgColor)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, null, tint = riskColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI RISK ANALYSIS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        ),
                        color = riskColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = userData.riskLevel.label,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = riskColor
                )
                Text(
                    text = when(userData.riskLevel) {
                        RiskLevel.HIGH -> "High environmental risks in ${userData.city}"
                        RiskLevel.MEDIUM -> "Moderate risks monitored in ${userData.city}"
                        RiskLevel.LOW -> "Stable conditions in ${userData.city}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = riskColor.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Fintech Cards Row
        Row(modifier = Modifier.fillMaxWidth()) {
            FintechSmallCard(
                modifier = Modifier.weight(1f),
                title = "Premium",
                value = "₹${userData.riskLevel.premium}",
                label = "/week",
                icon = Icons.Default.Shield,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            FintechSmallCard(
                modifier = Modifier.weight(1f),
                title = "Protected",
                value = "₹${(userData.dailyIncome.toDoubleOrNull() ?: 0.0) * 30 / 1000}k",
                label = "target",
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                color = Color(0xFF388E3C)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Activity Placeholder
        Text(
            text = "Active Protection",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Shield, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Parametric Rain Cover", fontWeight = FontWeight.Bold)
                    Text("Continuous AI monitoring active", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun FintechSmallCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 2.dp, start = 2.dp))
            }
        }
    }
}
