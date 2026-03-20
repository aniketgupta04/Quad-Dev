package com.example.quad_dev.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quad_dev.ui.viewmodel.GigViewModel
import com.example.quad_dev.ui.viewmodel.SimulationStep

@Composable
fun SimulationScreen(viewModel: GigViewModel) {
    val currentStep by viewModel.currentStep.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "AI Monitoring",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            )
            Text(
                text = "Simulating real-time disruption detection",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Central Animation Area with Pulsing Effect for Analyzing
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(
                        if (currentStep is SimulationStep.Analyzing) 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else 
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    },
                    label = "SimulationStep"
                ) { step ->
                    val iconModifier = if (step is SimulationStep.Analyzing) 
                        Modifier.size(120.dp).graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
                        else Modifier.size(120.dp)

                    when (step) {
                        is SimulationStep.Idle -> {
                            Icon(Icons.Default.Psychology, null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
                        }
                        is SimulationStep.Analyzing -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(140.dp), 
                                strokeWidth = 8.dp,
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        is SimulationStep.EventDetected -> {
                            Icon(Icons.Default.Cloud, null, modifier = Modifier.size(120.dp), tint = Color(0xFF607D8B))
                        }
                        is SimulationStep.PayoutTriggered -> {
                            if (step.isVerified) {
                                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(140.dp), tint = Color(0xFF388E3C))
                            } else {
                                Icon(Icons.Default.Cancel, null, modifier = Modifier.size(140.dp), tint = Color(0xFFD32F2F))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Status Message Area - Fintech Style Card
            AnimatedVisibility(
                visible = currentStep != SimulationStep.Idle,
                enter = slideInVertically { it / 2 } + fadeIn(),
                exit = slideOutVertically { it / 2 } + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (currentStep) {
                            is SimulationStep.PayoutTriggered -> if ((currentStep as SimulationStep.PayoutTriggered).isVerified) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                            is SimulationStep.EventDetected -> Color(0xFFFFF3E0)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (currentStep) {
                                is SimulationStep.Analyzing -> "AI Engine: Analyzing environmental data..."
                                is SimulationStep.EventDetected -> "INCIDENT DETECTED: Heavy Rain 🌧️"
                                is SimulationStep.PayoutTriggered -> if ((currentStep as SimulationStep.PayoutTriggered).isVerified) "Claim Automatically Triggered ⚡" else "Payout Suspended ⚠️"
                                else -> ""
                            },
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = when (currentStep) {
                                is SimulationStep.PayoutTriggered -> if ((currentStep as SimulationStep.PayoutTriggered).isVerified) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                                is SimulationStep.EventDetected -> Color(0xFFE65100)
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        
                        if (currentStep is SimulationStep.PayoutTriggered) {
                            val step = currentStep as SimulationStep.PayoutTriggered
                            Spacer(modifier = Modifier.height(12.dp))
                            if (step.isVerified) {
                                Text(
                                    text = "₹${String.format("%.2f", step.amount)}",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-1).sp
                                    ),
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "Credited to your linked account",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF1B5E20).copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "Location Verified ✅",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF1B5E20)
                                    )
                                }
                            } else {
                                Text(
                                    text = "₹${String.format("%.2f", step.amount)}",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-1).sp
                                    ),
                                    color = Color(0xFFB71C1C)
                                )
                                Text(
                                    text = "Transaction flagged for review",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFB71C1C).copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    color = Color(0xFFB71C1C).copy(alpha = 0.1f),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "Suspicious Activity Detected ⚠️",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFFB71C1C)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (currentStep == SimulationStep.Idle) {
                Button(
                    onClick = { viewModel.runSimulation() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        "Run AI Disruption Demo", 
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            } else if (currentStep is SimulationStep.PayoutTriggered) {
                TextButton(
                    onClick = { viewModel.resetSimulation() },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        "Reset System",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
