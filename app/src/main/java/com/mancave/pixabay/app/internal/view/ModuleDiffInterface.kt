package com.mancave.pixabay.app.internal.view

interface ModuleDiffInterface {

    /**
     * @return String that can be used to compare identity
     */
    fun compareString(): String

    /**
     * @return String that can be used to compare content
     */
    fun compareContentString(): String
}
