package com.boostcamp.sentialarm;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Created by 현기 on 2017-08-21.
 */

public class TestSet
{
    @Test
    public void test1(){

        String str1 = "java";
        String str2 = new String("java");

        assertSame(str1, str2.intern());
    }

    @Test
    public void test2(){
        System. out .println("testCase2");
        String text = "TEST";
        String subString = text.substring(0,2);
        assertFalse ("실패메시지","TE".equals(subString));
    }

    @Test
    public void test3(){

    }
}
