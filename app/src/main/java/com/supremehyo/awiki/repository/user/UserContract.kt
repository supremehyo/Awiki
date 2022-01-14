package com.supremehyo.awiki.repository.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tb_user")//테이블 이름
data class UserContract (
    //컬럼생성
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var interest: List<Long> //관심있는 문서들의 id list
)
