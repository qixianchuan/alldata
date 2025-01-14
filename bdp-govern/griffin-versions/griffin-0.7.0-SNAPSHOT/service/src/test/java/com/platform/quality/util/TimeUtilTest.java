/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package com.platform.quality.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TimeUtilTest {

    @Test
    public void testStr2LongWithPositive() {
        String time = "2hr3m4s";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "7384000");
    }

    @Test
    public void testStr2LongWithNegative() {
        String time = "-2hr3min4s";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "-7384000");
    }

    @Test
    public void testStr2LongWithNull() {
        String time = null;
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "0");
    }

    @Test
    public void testStr2LongWithDay() {
        String time = "1DAY";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "86400000");
    }

    @Test
    public void testStr2LongWithHour() {
        String time = "1h";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "3600000");
    }

    @Test
    public void testStr2LongWithMinute() {
        String time = "1m";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "60000");
    }

    @Test
    public void testStr2LongWithSecond() {
        String time = "1s";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "1000");
    }

    @Test
    public void testStr2LongWithMillisecond() {
        String time = "1ms";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "1");
    }

    @Test
    public void testStr2LongWithIllegalFormat() {
        String time = "1y2m3s";
        assertEquals(String.valueOf(TimeUtil.str2Long(time)), "123000");
    }

    @Test
    public void testFormat() {
        String format = "dt=#YYYYMMdd#";
        Long time = 1516186620155L;
        String timeZone = "GMT+8:00";
        assertEquals(TimeUtil.format(format, time, TimeZone
                .getTimeZone(timeZone)), "dt=20180117");
    }

    @Test
    public void testFormatWithDiff() {
        String format = "dt=#YYYYMMdd#/hour=#HH#";
        Long time = 1516186620155L;
        String timeZone = "GMT+8:00";
        assertEquals(TimeUtil.format(format, time, TimeZone
                .getTimeZone(timeZone)), "dt=20180117/hour=18");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithIllegalException() {
        String format = "\\#YYYYMMdd\\#";
        Long time = 1516186620155L;
        String timeZone = "GMT+8:00";
        TimeUtil.format(format, time, TimeZone.getTimeZone(timeZone));
    }

    @Test
    public void testGetTimeZone() {
        Map<String, String> tests = new HashMap<>();
        tests.put("", TimeZone.getDefault().getID());
        // standard cases
        tests.put("GMT", "GMT");
        tests.put("GMT+1", "GMT+01:00");
        tests.put("GMT+1:00", "GMT+01:00");
        tests.put("GMT+01:00", "GMT+01:00");
        tests.put("GMT-1", "GMT-01:00");
        tests.put("GMT-1:00", "GMT-01:00");
        tests.put("GMT-01:00", "GMT-01:00");
        // values pushed by UI for jobs
        tests.put("GMT1", "GMT");
        tests.put("GMT1:00", "GMT");
        tests.put("GMT01:00", "GMT");
        // values generated by UI for datasets in a past
        tests.put("UTC1", "GMT");
        tests.put("UTC1:00", "GMT");
        tests.put("UTC01:00", "GMT");
        tests.put("UTC-1", "GMT");
        tests.put("UTC-1:00", "GMT");
        tests.put("UTC-01:00", "GMT");
        // "named" time zones support
        tests.put("CST", "CST"); // supported
        tests.put("CDT", "GMT"); // not supported
        tests.put("America/Los_Angeles", "America/Los_Angeles"); // supported
        tests.forEach((input, expected) -> {
            String actual = TimeUtil.getTimeZone(input).getID();
            assertEquals(String.format("For input: %s", input), expected, actual);
        });
    }


}
