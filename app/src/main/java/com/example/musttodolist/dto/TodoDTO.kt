package com.example.musttodolist.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "todoTable")
class TodoDTO (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id : Long = 0,
    @ColumnInfo(name = "content") var content : String,
    @ColumnInfo(name = "time") var time : String,
    @ColumnInfo(name = "complete") var complete : Boolean,
    @ColumnInfo(name = "isAlarmOn") var isAlarmOn : Boolean,
    @ColumnInfo(name = "alarmTime") var alarmTime : String

) : Serializable{
    //직렬화
    //DTO는 해당 서비스에서 내부적으로 사용되는 오브젝트이기 때문에 별도의 직렬화 작업이 필요하지 않을 수도 있지만,
    // DTO 객체를 다른쪽에서 사용할 수도 있었기 때문에 직렬화 코드가 남겨져 있었던거 같습니다.
    // DB 저장시 직렬화 없이 저장해도 문제가 되지 않을 수도 있지만, 해당 객체가 변경되었을 경우,
    // DB있던 내용과 새롭게 변경된 객체의 serialVersionUID가 다르면, 사용할 수 없게 될 수도 있습니다.
}
