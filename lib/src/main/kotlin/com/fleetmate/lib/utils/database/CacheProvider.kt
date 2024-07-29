package com.fleetmate.lib.utils.database


abstract class CacheProvider {
    protected abstract var valid: Boolean
    open fun invalidate() {
        valid = false
    }
}