package com.faradaj.patternededittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import com.faradaj.patternededittext.utils.PatternUtils;

public class PatternedEditText extends EditText {

    private String mSpecialChar;
    private String mPattern;

    private String mRawText = "";

    public PatternedEditText(Context context) {
        super(context);
    }

    public PatternedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PatternedEditText);
        mPattern = a.getString(R.styleable.PatternedEditText_pattern);
        mSpecialChar = a.getString(R.styleable.PatternedEditText_specialChar);
        if (mSpecialChar == null) {
            mSpecialChar = "#";
        }
        boolean showHint = a.getBoolean(R.styleable.PatternedEditText_showPatternAsHint, false);
        a.recycle();

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPattern.length())});

        if (showHint) {
            setHint(mPattern);
        }

        final TextWatcher textWatcher = new TextWatcher() {
            private boolean mForcing = false;
            private StringBuilder sb;

            private boolean isDeleting = false;

            private int differenceCount = 0;
            public int toBeSetCursorPosition = 0;
            public int mBeforeTextLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mBeforeTextLength = s.length();
                if (!mForcing) {
                    sb = new StringBuilder();
                    differenceCount = PatternUtils.getDifferenceCount(s.toString().substring(0, getSelectionStart()), mPattern, mSpecialChar.charAt(0));
                    sb.append(mRawText);
                    if (after == 0) {
                        isDeleting = true;
                        try {
                            sb.delete(getSelectionEnd() - count - differenceCount, getSelectionEnd() - differenceCount);
                        } catch (IndexOutOfBoundsException e) {
                            //Do nothing. User tried to delete unremovable char(s) of pattern.
                        }
                    } else {
                        isDeleting = false;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mForcing) {
                    if (!isDeleting) {
                        try {
                            int from = getSelectionEnd() - count - differenceCount;
                            if (from < 0) {
                                from = 0;
                            }
                            sb.insert(from, s.subSequence(start, start + count));
                        } catch (StringIndexOutOfBoundsException e) {
                            Log.e("PatternedEditText: ", e.toString());
                            //getSelectionEnd() returns 0 after screen rotation.
                            //Added to handle filling EditText after rotation.
                            //onRestoreInstanceState() is responsible for setting text.
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mForcing) {
                    mForcing = true;
                    mRawText = sb.toString();
                    String convertedText;
                    if (PatternUtils.isTextAppliesPattern(mRawText, mPattern, mSpecialChar.charAt(0))) {
                        convertedText = mRawText;
                        mRawText = PatternUtils.convertPatternedTextToText(mRawText, mPattern, mSpecialChar.charAt(0));
                    } else {
                        convertedText = PatternUtils.convertTextToPatternedText(mRawText, mPattern, mSpecialChar.charAt(0));
                    }
                    toBeSetCursorPosition = getSelectionStart() + convertedText.length() - s.length();
                    if (mBeforeTextLength == 0) {
                        toBeSetCursorPosition = convertedText.length();
                    }
                    s.clear();
                    s.append(convertedText);
                    try {
                        if (isDeleting) {
                            if (toBeSetCursorPosition < convertedText.length()) {
                                ++toBeSetCursorPosition;
                            }
                        } else if (toBeSetCursorPosition != convertedText.length()) {
                            --toBeSetCursorPosition;
                        }
                        setSelection(toBeSetCursorPosition);
                    } catch (IndexOutOfBoundsException e) {
                        Log.e("PatternedEditText: ", e.toString());
                    }
                    mForcing = false;
                }
            }
        };

        addTextChangedListener(textWatcher);
    }

    public PatternedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.mRawText = "";
        super.setText(text, type);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        return new PetSavedState(superState, mRawText);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        PetSavedState savedState = (PetSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mRawText = savedState.getRealText();
        setText(mRawText);
    }

    public String getRawText() {
        return mRawText;
    }

    public String getSpecialChar() {
        return mSpecialChar;
    }

    public void setSpecialChar(String specialChar) {
        this.mSpecialChar = specialChar;
    }

    public String getPattern() {
        return mPattern;
    }

    public void setPattern(String pattern) {
        this.mPattern = pattern;
    }
}
