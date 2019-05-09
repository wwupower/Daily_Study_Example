---
title: Colletions.sort(list)总结
date: 2018-07-30 22:12:34
tags: 笔记
categories: java基础
---

Colletions.sort()实际会将list转为数组，然后调用Arrays.sort()，排完了再转回List。
<!--more-->
在JDK8里，List有自己的sort()方法了，像ArrayList就直接用自己内部的数组来排，而LinkedList, CopyOnWriteArrayList还是要复制出一份数组。

> jDK6

而Arrays.sort()，对原始类型(int[],double[],char[],byte[])，
JDK6里用的是快速排序，对于对象类型(Object[])，JDK6则使用归并排序。

> JDK7

到了JDK7，快速排序升级为双基准快排(双基准快排 vs 三路快排)；归并排序升级为归并排序的改进版TimSort，一个JDK的自我进化。

> JDK8

再到了JDK8， 对大集合增加了Arrays.parallelSort()函数，使用fork-Join框架，充分利用多核，对大的集合进行切分然后再归并排序，而在小的连续片段里，依然使用TimSort与DualPivotQuickSort。
 
