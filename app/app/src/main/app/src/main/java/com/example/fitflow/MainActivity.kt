package com.example.fitflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

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

// ----------------------------------------------------
// 在此处粘贴我们上一轮写好的完整 Compose 代码：
// 包含 FitFlowHomeScreen(), PeriodEntranceCard(), 
// CalorieRingCard(), MenstrualSheetContent() 等全部组件
// ----------------------------------------------------
