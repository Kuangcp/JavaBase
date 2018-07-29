package com.github.kuangcp.sort;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by https://github.com/kuangcp
 * 将所有的排序都运行一边进行比较
 *
 * @author kuangcp
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    BoxTest.class,
    BubbleTest.class,
    InsertTest.class,
    QuickTest.class,
    SelectTest.class,
    ShellTest.class,
    ShowTest.class
})
public class AllSortTest {

}
