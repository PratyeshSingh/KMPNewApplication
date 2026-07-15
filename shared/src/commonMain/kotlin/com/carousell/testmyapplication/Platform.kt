package com.carousell.testmyapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform