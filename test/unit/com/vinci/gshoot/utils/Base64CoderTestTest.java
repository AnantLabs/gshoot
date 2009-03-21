package com.vinci.gshoot.utils;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Base64CoderTestTest {
    @Test
    public void should_encode_and_decode_common() {
        String special = "abde 3f=-";
        assertEquals(special, Base64Coder.decodeString(Base64Coder.encodeString(special)));
    }
    @Test
    public void should_encode_and_decode_special_chars() {
        String special = "!@#$%^&*()-=_+";
        assertEquals(special, Base64Coder.decodeString(Base64Coder.encodeString(special)));
    }

    @Test
    public void should_encode_and_decode_chinese() {
        String cn = "这是中文!@#";
        assertEquals(cn, Base64Coder.decodeString(Base64Coder.encodeString(cn)));
    }
}
