package com.peashoot.blog;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

public class CollectionComponentTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @SuppressWarnings("unchecked")
    public void testCheckedList() {
        List myList = new ArrayList();
        myList.add("one");
        myList.add("two");
        myList.add("three");
        myList.add("four");
        List chkList = Collections.checkedList(myList, String.class);
        System.out.println("Checked list content: " + chkList);
        //您可以向myList对象添加任何类型的元素
        myList.add(10);
        //您不能向chkList对象添加任何类型的元素，否则将引发ClassCastException
        thrown.expect(ClassCastException.class);
        chkList.add(10); //throws ClassCastException
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDisjoint() {
        // Arrays.asList 出来的ArrayList不是java.util.ArrayList，不能使用add等方法
        List evenList = new ArrayList(Arrays.asList(2, 4, 6, 8));
        List primeList = new ArrayList(Arrays.asList(3, 5, 7));
        // disjoint方法返回的是两个集合中是否没有交集
        Assert.assertTrue(Collections.disjoint(evenList, primeList));
        primeList.add(2);
        Assert.assertFalse(Collections.disjoint(evenList, primeList));
    }

    @Test
    public void testFrequency() {
        List<Integer> triangleList = new ArrayList<>(Arrays.asList(1, 1, 2, 1, 1, 3, 3, 1, 1, 4, 6, 4, 1, 1, 5, 10, 10, 5, 1));
        // frequency 计算出当前元素在列表中出现的次数
        Assert.assertEquals(Collections.frequency(triangleList, 1), 9);
        // fill 将列表中元素都替换成目标元素
        Collections.fill(triangleList, 31);
        // nCopies 创建一个不可变长的CopyList，将元素o重复n次
        Integer[] expectArray = Collections.nCopies(19, 31).toArray(new Integer[0]);
        Assert.assertArrayEquals(triangleList.toArray(new Integer[0]), expectArray);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRotate() {
        List increaseList = Arrays.asList(9, 2, 5, 4, 8, 7, 1, 6, 0, 3);
        // 排序
        Collections.sort(increaseList);
        System.out.println("After sort: " + increaseList);
        // distance > 0 右旋
        Collections.rotate(increaseList, 4);
        System.out.println("After right rotate: " + increaseList);
        // distance < 0 左旋
        Collections.rotate(increaseList, -6);
        System.out.println("After left rotate: " + increaseList);
        // 翻转
        Collections.reverse(increaseList);
        System.out.println("After reverse: " + increaseList);
        // 打乱
        Collections.shuffle(increaseList);
        System.out.println("After shuffle: " + increaseList);
    }

    @Test
    public void testForeachRemove() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            String item = iterator.next();
//            if ("1".equals(item)) {
//                iterator.remove();
//            }
//        }
        thrown.expect(ConcurrentModificationException.class);
        for (String item : list) {
            if ("2".equals(item)) {
                list.remove(item);
            }
        }
        Assert.assertEquals(list.size(), 1);
    }
}
