package com.faradaj.patternededittext.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PatternUtilsTest {

    private String mSpecialChar = "#";
    private String mPattern;
    private ArrayList<Span> mSpans;

    @Test
    public void testConvertTextToPatternedText() {
        char specialChar = mSpecialChar.charAt(0);

        String givenString;

        mPattern="#-#";
        givenString = "a";
        assertEquals("a", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));

        mPattern="#-#";
        givenString = "ab";
        assertEquals("a-b", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));

        mPattern="#-#)#";
        givenString = "abc";
        assertEquals("a-b)c", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));

        mPattern="#-#)#)";
        givenString = "abc";
        assertEquals("a-b)c)", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));

        mPattern="#### #### #### ####";
        givenString = "1234123412341234";
        assertEquals("1234 1234 1234 1234", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));

        mPattern="(###)-(### ## ##)";
        givenString = "5321234567";
        assertEquals("(532)-(123 45 67)", PatternUtils.convertTextToPatternedText(givenString, mPattern, specialChar));
    }

    @Test
    public void testPreviousDifference() {
        char specialChar = mSpecialChar.charAt(0);

        String patternedText = "ab-cd";
        mPattern = "##-##";
        assertEquals(1, PatternUtils.getDifferenceCount(patternedText, mPattern, specialChar));

        patternedText = "a-b-cd";
        mPattern = "#-#-##";
        assertEquals(2, PatternUtils.getDifferenceCount(patternedText, mPattern, specialChar));

        patternedText = "a-b-c-d";
        mPattern = "#-#-#-#";
        assertEquals(3, PatternUtils.getDifferenceCount(patternedText, mPattern, specialChar));
    }

    @Test
    public void testTextAppliesPattern() {
        char specialChar = mSpecialChar.charAt(0);

        String patternedText = "ab-cd";
        mPattern = "##-##";
        assertTrue(PatternUtils.isTextAppliesPattern(patternedText, mPattern, specialChar));

        patternedText = "ab-c";
        mPattern = "##-##";
        assertTrue(PatternUtils.isTextAppliesPattern(patternedText, mPattern, specialChar));

        patternedText = "abc";
        mPattern = "##-##";
        assertTrue(!PatternUtils.isTextAppliesPattern(patternedText, mPattern, specialChar));

        patternedText = "(532)-(123 45 67)";
        mPattern="(###)-(### ## ##)";
        assertTrue(PatternUtils.isTextAppliesPattern(patternedText, mPattern, specialChar));
    }

    @Test
    public void testConvertPatternedTextToText() {
        char specialChar = mSpecialChar.charAt(0);

        String givenString;

        mPattern="#-#";
        givenString = "a-";
        assertEquals("a", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));

        mPattern="#-#";
        givenString = "a-b";
        assertEquals("ab", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));

        mPattern="#-#)#";
        givenString = "a-b)c";
        assertEquals("abc", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));

        mPattern="#-#)#)";
        givenString = "a-b)c)";
        assertEquals("abc", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));

        mPattern="#### #### #### ####";
        givenString = "1234 1234 1234 1234";
        assertEquals("1234123412341234", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));

        mPattern="(###)-(### ## ##)";
        givenString = "(532)-(123 45 67)";
        assertEquals("5321234567", PatternUtils.convertPatternedTextToText(givenString, mPattern, specialChar));
    }

    @Test
     public void testBasicPattern() {
        mPattern = "######";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(0, 6));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testMiddleSeperatedPattern() {
        mPattern = "### ###";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(2);
        spans.add(new Span(0, 3));
        spans.add(new Span(4, 3));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testMiddleSeperatedPattern2() {
        mPattern = "### ##";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(2);
        spans.add(new Span(0, 3));
        spans.add(new Span(4, 2));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testStartSeperatedPattern() {
        mPattern = " #####";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(1, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testStartSeperatedPattern2() {
        mPattern = " (#####";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(2, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testStartSeperatedPattern3() {
        mPattern = " (+#####";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(3, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testEndSeperatedPattern() {
        mPattern = "##### ";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(0, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testEndSeperatedPattern2() {
        mPattern = "##### )";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(0, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testEndSeperatedPattern3() {
        mPattern = "##### +)";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(1);
        spans.add(new Span(0, 5));

        assertEquals(spans, mSpans);
    }

    @Test
    public void testComplicatedPattern() {
        mPattern = "(###)-(### ## ##)";

        mSpans = PatternUtils.generateSpansFromPattern(mSpecialChar, mPattern);

        ArrayList<Span> spans = new ArrayList<Span>(4);
        spans.add(new Span(1, 3));
        spans.add(new Span(7, 3));
        spans.add(new Span(11, 2));
        spans.add(new Span(14, 2));

        assertEquals(spans, mSpans);
    }
}
