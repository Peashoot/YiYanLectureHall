package com.peashoot.blog;

import com.peashoot.blog.batis.enums.PermissionEnum;
import org.junit.Test;

public class GenericTest {
    @Test
    public void testArrayClass() {
        Class c = PermissionEnum[].class;
    }
}
