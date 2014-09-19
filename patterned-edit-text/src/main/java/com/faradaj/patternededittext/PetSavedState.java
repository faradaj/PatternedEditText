package com.faradaj.patternededittext;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class PetSavedState extends View.BaseSavedState {

    private String mRealText;

    protected PetSavedState(Parcel source) {
        super(source);
        mRealText = source.readString();
    }

    protected PetSavedState(Parcelable superState, String realText) {
        super(superState);
        mRealText = realText;
    }

    protected String getRealText() {
        return mRealText;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        super.writeToParcel(destination, flags);
        destination.writeString(mRealText);
    }
}
