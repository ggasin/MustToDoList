package com.example.musttodolist.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelTable")
class LevelDTO (
    @ColumnInfo(name = "id" , defaultValue = "0") @PrimaryKey var id : Long = 0,
    @ColumnInfo(name = "level", defaultValue = "1") var level : Int = 1,
    @ColumnInfo(name = "upGaugeSize", defaultValue = "20") var upGaugeSize : Int = 20,
    @ColumnInfo(name = "currentGauge", defaultValue = "0") var currentGauge : Int = 0,
    @ColumnInfo(name = "max", defaultValue = "100") var max : Int = 100,
)


