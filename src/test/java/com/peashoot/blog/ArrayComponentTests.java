package com.peashoot.blog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayComponentTests {

    private Integer[] intArray = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

    private Integer[] intArray2 = {1, 3, 5, 7, 9, 0, 2, 4, 6, 8};
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAsList() {
        List<Integer> list = Arrays.asList(intArray);
        // Arrays.asList() 返回的ArrayList是Arrays的一个内部类 java.util.Arrays$ArrayList， 不是 java.util.ArrayList
        Assert.assertNotEquals(list.getClass(), ArrayList.class);
        int[] myArray = { 1, 2, 3 };
        // Arrays.asList()是泛型方法，传入的对象必须是对象数组。
        // 当传入一个原生数据类型数组时，Arrays.asList() 的真正得到的参数就不是数组中的元素，而是数组对象本身
        List myList = Arrays.asList(myArray);
        System.out.println(myList.size());//1
        System.out.println(myList.get(0));//数组地址值
        thrown.expect(ArrayIndexOutOfBoundsException.class);
        System.out.println(myList.get(1));//报错：ArrayIndexOutOfBoundsException
        int [] array=(int[]) myList.get(0);
        System.out.println(array[0]);//1
        // 数组转列表的方法
        myList = new ArrayList<>(Arrays.asList(intArray));
        myList = Arrays.stream(intArray).collect(Collectors.toList());
        myList = Arrays.stream(myArray).boxed().collect(Collectors.toList());
        myList = ImmutableList.copyOf(intArray); // 不可变列表
        myList = Lists.newArrayList(intArray);
    }

    @Test
    public void testMultiMethods() {
        Arrays.sort(intArray);
        // 二分查找前必须先 sort
        int index = Arrays.binarySearch(intArray, 5);
        System.out.println("Arrays.binarySearch: index " + index + " value: " + intArray[index]);
        // 复制从0到newLength的数组
        Integer[] copy = Arrays.copyOf(intArray, 5);
        System.out.println("Arrays.copyOf: " + Arrays.toString(copy));
        // 复制从from到to的数组
        copy = Arrays.copyOfRange(intArray, 5, 8);
        System.out.println("Arrays.copyOfRange: " + Arrays.toString(copy));
        // deepEqual 判断是否相等，如果数组的元素也是数据类型，则对每一个元素的元素也进行比较
        boolean same = Arrays.deepEquals(intArray, intArray2);
        // equal 只比较地址和元素是否相等
        same = Arrays.equals(intArray, intArray2);
        Integer[][] arr = new Integer[][]{intArray, intArray2};
        // 返回指定数组的“深度内容”的字符串表示形式。 如果数组包含其他数组作为元素，则字符串表示包含其内容等等
        System.out.println("Arrays.deepToString: " + Arrays.deepToString(arr));
        System.out.println("Arrays.toString: " + Arrays.toString(arr));
        // 填充数组，类似于C语言 memset
        Arrays.fill(intArray, 1231);
        System.out.println("Arrays.fill: " + Arrays.toString(intArray));
    }

    @Test
    public void testParallelPrefix() {
        System.out.print("Arrays.parallelPrefix: ");
        // parallelPrefix 方法相当于C# Linq 操作中的 Aggregate 方法
        // @FunctionalInterface 标签将接口转义成类似C#中的委托
        // :: 可以访问类型的对象方法(对象::方法名)、静态方法(类名::方法名)和构造方法(::new)，可以对应成@FunctionalInterface标识的接口
//        // 写法1
//        Arrays.parallelPrefix(intArray, new BinaryOperator<Integer>(){
//            @Override
//            public Integer apply(Integer integer, Integer integer2) {
//                return integer + integer2;
//            }
//        });
//        // 写法2
//        Arrays.parallelPrefix(intArray, (integer, integer2) -> integer + integer2);
        // 写法3
        Arrays.parallelPrefix(intArray, Integer::sum);
        System.out.println(Arrays.toString(intArray));
    }

    @Test
    public void testParallelSelectAll() {
        System.out.print("Arrays.parallelSelectAll: ");
        // parallelSetAll 对每个元素进行处理，相当于C# Linq 操作中的 Select 方法
        Arrays.parallelSetAll(intArray, (input) -> input << 1);
        System.out.println(Arrays.toString(intArray));
    }

    @Test
    public void testSort() {
        // 长度不长时使用TimSort
        Arrays.parallelSort(intArray);
        System.out.println(Arrays.toString(intArray));
        // 一般情况下使用TimSort
        Arrays.sort(intArray2);
        System.out.println(Arrays.toString(intArray2));
        Assert.assertTrue(Arrays.equals(intArray, intArray2));
    }
}
