package com.drawiin.mymovielist.core.test

import com.google.common.truth.Truth

infix fun Any.assertIsEqualTo(other: Any) = Truth.assertThat(this)
    .isEqualTo(other)
