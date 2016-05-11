package com.faradaj.patternededittext;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.EditText;

public class PatternedEditText extends EditText {

    private PatternedViewHelper patternedViewHelper;

    public PatternedEditText(Context context) {
        super(context);
        init(null);
    }

    public PatternedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public PatternedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        patternedViewHelper=new PatternedViewHelper(this);
        patternedViewHelper.resolveAttributes(attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(patternedViewHelper!=null) {
            patternedViewHelper.setText();
        }
        super.setText(text, type);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return patternedViewHelper.onSaveInstanceState(superState);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        BaseSavedState savedState = (BaseSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        patternedViewHelper.onRestoreInstanceState(state);
    }

    public String getRawText() {
        return patternedViewHelper.getRawText();
    }

    public String getSpecialChar() {
        return patternedViewHelper.getSpecialChar();
    }

    public void setSpecialChar(String specialChar) {
        patternedViewHelper.setSpecialChar(specialChar);
    }

    public String getPattern() {
        return patternedViewHelper.getPattern();
    }

    public void setPattern(String pattern) {
        patternedViewHelper.setPattern(pattern);
    }
}
