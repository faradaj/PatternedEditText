package com.faradaj.patternededittext.utils;

import java.util.ArrayList;

public class PatternUtils {

    public static String convertTextToPatternedText(String text, String pattern, char specialChar) {
        StringBuilder sb = new StringBuilder();

        int patternLength = pattern.length();
        int textLength = text.length();
        int patternAdditionCount = 0;

        for (int i = 0; i < textLength + patternAdditionCount; ++i) {
            if (patternLength > i && pattern.charAt(i) != specialChar) {
                sb.append(pattern.charAt(i));
                ++patternAdditionCount;
            } else {
                sb.append(text.charAt(i - patternAdditionCount));
            }

            if (i + 1 == textLength + patternAdditionCount) {
                if (patternLength > i + 1) {
                    if (pattern.substring(i + 1).indexOf(specialChar) == -1) {
                        sb.append(pattern.substring(i + 1));
                    }
                }
            }
        }

        return sb.toString();
    }

    public static int getDifferenceCount(String patternedText, String pattern, char specialChar) {
        int patternedTextLength = patternedText.length();
        int differenceCounter = 0;

        for (int i = 0; i < patternedTextLength; ++i) {
            if (pattern.charAt(i) != specialChar) {
                ++differenceCounter;
            }
        }

        return differenceCounter;
    }

    public static boolean isTextAppliesPattern(String realText, String pattern, char specialChar) {
        int patternLength = pattern.length();
        int realTextLength = realText.length();

        for (int i = 0; i < patternLength; ++i) {
            if (realTextLength > i) {
                if (pattern.charAt(i) != specialChar) {
                    if (realText.charAt(i) != pattern.charAt(i)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static String convertPatternedTextToText(String patternedText, String pattern, char specialChar) {
        StringBuilder sb = new StringBuilder();

        int patternedTextLength = patternedText.length();

        for (int i = 0; i < patternedTextLength; ++i) {
            if (pattern.charAt(i) == specialChar) {
                sb.append(patternedText.charAt(i));
            }
        }

        return sb.toString();
    }

    public static ArrayList<Span> generateSpansFromPattern(String specialChar, String pattern) {
        ArrayList<Span> spans = new ArrayList<Span>();

        int offset = 0;
        int length = 0;

        int patternLength = pattern.length();

        for (int i = 0; i < patternLength; ++i) {
            if (pattern.substring(i, i + 1).equals(specialChar)) {
                if (length == 0) {
                    offset = i;
                }
                ++length;
            }

            if ((i + 1 < patternLength && !pattern.substring(i+1, i+2).equals(specialChar)) || i + 1 == patternLength) {
                if (!(offset == 0 && length == 0)) {
                    spans.add(new Span(offset, length));
                }
                offset = 0;
                length = 0;
            }
        }

        return spans;
    }
}
