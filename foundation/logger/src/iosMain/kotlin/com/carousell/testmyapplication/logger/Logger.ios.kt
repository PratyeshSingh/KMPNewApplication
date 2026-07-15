package com.carousell.testmyapplication.logger

import platform.Foundation.NSLog

actual fun logMessage(message: String, tag: String) {
    NSLog("[%s] %s", tag, message)
}
