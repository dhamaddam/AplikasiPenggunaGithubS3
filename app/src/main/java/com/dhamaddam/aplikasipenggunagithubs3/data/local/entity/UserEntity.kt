package com.dhamaddam.aplikasipenggunagithubs3.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class UserEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int? = null,

    @field:ColumnInfo(name = "login")
    var login: String?,

    @field:ColumnInfo(name = "type")
    val type: String? = null,

    @field:ColumnInfo(name = "avatar_url")
    var avatar: String? = null,

    @field:ColumnInfo(name = "isfavorite")
    var isfavorite: Boolean
)