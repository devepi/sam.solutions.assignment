package com.mancave.pixabay.app.internal.view

interface UIDiffInterface {
    val id: String
    fun contentEquals(uiDiffInterface: UIDiffInterface) = equals(uiDiffInterface)
}
