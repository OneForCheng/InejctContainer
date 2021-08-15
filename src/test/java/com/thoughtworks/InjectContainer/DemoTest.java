package com.thoughtworks.InjectContainer;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class DemoTest {
    // 一定要记住声明为 public class， 不然获取字段的值为 null
    public class Splitor {
        public String firstName;
        public String lastName;

        public Splitor(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public String getName() {
        return "司马迁";
    }

    public Splitor split(String fullName) {
        String[] splits = fullName.split(" ");
        return new Splitor(splits[0], splits[1]);
    }
}