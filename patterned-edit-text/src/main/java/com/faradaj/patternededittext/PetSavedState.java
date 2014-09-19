package com.faradaj.patternededittext;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class PetSavedState extends View.BaseSavedState {

    private String mRawText;

    protected PetSavedState(Parcel source) {
        super(source);
        mRawText = source.readString();
    }

    protected PetSavedState(Parcelable superState, String rawText) {
        super(superState);
        mRawText = rawText;
    }

    protected String getRealText() {
        return mRawText;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        super.writeToParcel(destination, flags);
        destination.writeString(mRawText);
    }
}
