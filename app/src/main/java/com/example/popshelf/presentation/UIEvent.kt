package com.example.popshelf.presentation


/***
 * Class which represents individual states for UI events.
 */
sealed class UIEvent{

    /**
     * Event which is called when navigation should take user back.
     */
    object NavigateBack : UIEvent()


    /**
     * Error state with message which can be displayed.
     * @param message - message which can be displayed as error message.
     */
    data class Error(val message: String) : UIEvent()
}
