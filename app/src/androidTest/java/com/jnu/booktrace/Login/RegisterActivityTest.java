package com.jnu.booktrace.Login;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

public class RegisterActivityTest  {
    @Rule
    public ActivityTestRule<RegisterActivity> registerActivityActivityTestRule =
            new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void TestRegister_IsTrue(){
        
    }
}