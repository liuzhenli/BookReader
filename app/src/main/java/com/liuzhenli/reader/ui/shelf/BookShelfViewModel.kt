package com.liuzhenli.reader.ui.shelf

import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.reader.ui.discover.ImportLocalBookState

class BookShelfViewModel(initialState: ImportLocalBookState) :
    MavericksViewModel<ImportLocalBookState>(initialState) {
}