package com.example.fitflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PrimaryMint = Color(0xFF14C9A0)
val BackgroundGray = Color(0xFFF8FAFC)
val CardBackground = Color.White
val PeriodPink = Color(0xFFFF758F)
val PeriodLightPink = Color(0xFFFFF0F3)
val TextDark = Color(0xFF1E293B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FitFlowHomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitFlowHomeScreen() {
    var showMenstrualSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentPhase by remember { mutableStateOf("黄体期") }
    var daysUntilNext by remember { mutableStateOf(6) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("8月13日", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("⚙️", fontSize = 20.sp)
                }
            }

            item {
                PeriodEntranceCard(
                    currentPhase = currentPhase,
                    daysUntilNext = daysUntilNext,
                    onClick = { showMenstrualSheet = true }
                )
            }

            item {
                CalorieRingCard(consumedKcal = 1280, targetKcal = 1800, burnedKcal = 320)
            }

            item {
                MacroCardGroup(
                    carbCurrent = 120, carbTarget = 200,
                    proteinCurrent = 80, proteinTarget = 150,
                    fatCurrent = 40, fatTarget = 80
                )
            }

            item {
                WeightRecordQuickCard(lastWeight = "68.5kg", diffText = "-0.3kg")
            }
        }

        if (showMenstrualSheet) {
            ModalBottomSheet(
                onDismissRequest = { showMenstrualSheet = false },
                sheetState = sheetState,
                containerColor = CardBackground,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                MenstrualSheetContent(
                    currentPhase = currentPhase,
                    onSave = { showMenstrualSheet = false }
                )
            }
        }
    }
}

@Composable
fun PeriodEntranceCard(currentPhase: String, daysUntilNext: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PeriodLightPink),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌸", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "当前处于：$currentPhase",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "预估距离下次经期还有 $daysUntilNext 天 · 水肿高发期",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PeriodPink
            ) {
                Text(
                    text = "记一笔",
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun CalorieRingCard(consumedKcal: Int, targetKcal: Int, burnedKcal: Int) {
    val remainingKcal = targetKcal - consumedKcal + burnedKcal
    val progress = (consumedKcal.toFloat() / targetKcal.toFloat()).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(170.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    drawArc(
                        color = Color(0xFFE2E8F0),
                        startAngle = -90f, sweepAngle = 360f,
                        useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = PrimaryMint,
                        startAngle = -90f, sweepAngle = 360f * progress,
                        useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$remainingKcal", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("kcal剩余", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MetricSubItem(label = "已摄入", value = "${consumedKcal}kcal")
                MetricSubItem(label = "目标", value = "${targetKcal}kcal", isBold = true)
                MetricSubItem(label = "已消耗", value = "${burnedKcal}kcal")
            }
        }
    }
}

@Composable
fun MacroCardGroup(
    carbCurrent: Int, carbTarget: Int,
    proteinCurrent: Int, proteinTarget: Int,
    fatCurrent: Int, fatTarget: Int
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MacroCard("🌾 碳水化合物", carbCurrent, carbTarget, Color(0xFFFFB020), Modifier.weight(1f))
        MacroCard("🫘 蛋白质", proteinCurrent, proteinTarget, PrimaryMint, Modifier.weight(1f))
        MacroCard("💧 脂肪", fatCurrent, fatTarget, Color(0xFFFF5252), Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenstrualSheetContent(currentPhase: String, onSave: () -> Unit) {
    var isWaterRetention by remember { mutableStateOf(false) }
    var isCravingSweet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "生理期与代谢记录",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PeriodLightPink,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "处于【$currentPhase】时体内水分易滞留，建议目标摄入可适当放宽 100~150 kcal，避免极端的防焦虑节食。",
                    fontSize = 13.sp,
                    color = TextDark,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("今日身体状态标记", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterChip(
                selected = isWaterRetention,
                onClick = { isWaterRetention = !isWaterRetention },
                label = { Text("💧 感觉水肿/浮肿") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PeriodPink, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = isCravingSweet,
                onClick = { isCravingSweet = !isCravingSweet },
                label = { Text("🍰 极度想吃甜食") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PeriodPink, selectedLabelColor = Color.White)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSave,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("保存并更新今日建议", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MetricSubItem(label: String, value: String, isBold: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium, color = TextDark)
    }
}

@Composable
fun MacroCard(title: String, current: Int, target: Int, color: Color, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text("${current}g / ${target}g", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) },
                color = color,
                trackColor = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth().height(6.dp),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun WeightRecordQuickCard(lastWeight: String, diffText: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚖️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("今日体重打卡", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("较昨日 $diffText", fontSize = 12.sp, color = PrimaryMint)
                }
            }
            Text(lastWeight, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}
