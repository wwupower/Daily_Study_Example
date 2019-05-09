---
title: hashMap底层源码解读.md
date: 2018-07-16 18:25:08
tags: java 源码
categories: java基础
---

### hashCode() 说起

hashCode 是jdk根据对象的地址或者字符串或者数字算出来的int类型的数值
<!--more-->
hashCode 契约

这个契约在 hashCode 方法的 JavaDoc 中进行了阐述。它可以大致的归纳为下面几点：

1.在一个运行的进程中，相等的对象必须要有相同的哈希码

2.请注意这并不意味着以下常见的误解：

3.不相等的对象一定有着不同的哈希码——错！

4.有同一个哈希值的对象一定相等——错！

不同对象的数量经常比可能的哈希吗的数量 (2^32)更大；

### 什么是 `HashMap` ? 主题

 `HashMap` 是用于储存（key-value）键值对集合，每一个键值对应 `Entry`，这些键储存在一个数据中，数据是 `HashMap` 的主干。
内部是通过数组与链表实现的,可以理解为“链表的数组”。


### `put` 操作做了什么？

```
    public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        //计算获取hash值
        int hash = hash(key.hashCode());
        //获取此次hash值在数组（bucket）的下标位置
        int i = indexFor(hash, table.length);
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }
    
    void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K,V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }

```
1、可以看出 `put` 储存对象时候，先调用 `hashCode` 计算 `hash` 从而得到`bucket` 位置。

2、如果索引碰撞，遍历链表，判断是否有 key 存在，若存在更新 value 值。（不过在JDK1.8中链表元素超过8个时，会把链表结构转换成红黑树结构存储，提高查询的性能）

3.若未碰撞，直接放入表中。

4.放入前，如果元素个数超过负载因子的比例，则进行rehash，扩容，之会插入。

5.null key的元素永远会放到index为0的链表里面。

注意：当新加是达到量的阀值（ threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR) ） 默认容器大小16*负载因子0.75f 则开始扩容。容量大小是之前的2倍。


### hash算法是什么？
```
    JDK1.7
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    This function ensures that hashCodes that differ only by
    
    JDK1.8
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
```
当数组长度为2的n次幂的时候，不同的key算得得index相同的几率较小，那么数据在数组上分布就比较均匀，也就是说碰撞的几率小，相对的，查询的时候就不用遍历某个位置上的链表。

知识点：
当length总是2的n次方时，  h & (length - 1)   等价于   hash对length取模      ，但是&比%具有更高的效率；
在n - 1为15(0x1111)时，其实散列真正生效的只是低4bit的有效位，容易碰撞；

JDK1.8
通过hashCode()的高16位异或低16位实现的：(h = k.hashCode()) ^ (h >>> 16)，主要是从速度、功效、质量来考虑的，这么做可以在数组table的length比较小的时候，也能保证考虑到高低Bit都参与到Hash的计算中，同时不会有太大的开销。


### modCount干什么用？
HashMap中有一个成员变量modCount，这个用来实现“fast-fail”机制（也就是快速失败）。所谓快速失败就是在并发集合中，其进行迭代操作时，若有其他线程对其结构性的修改，这是迭代器会立马感知到，当 expectedModCount！= modCount，立刻抛出ConcurrentModificationException异常，不需要等待迭代完成之后才告诉你已经出错。



### get 如何实现

```
    public V get(Object key) {
        if (key == null)
            return getForNullKey();
        int hash = hash(key.hashCode());
        for (Entry<K,V> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
                return e.value;
        }
        return null;
    }
    
    private V getForNullKey() {
        for (Entry<K,V> e = table[0]; e != null; e = e.next) {
            if (e.key == null)
                return e.value;
        }
        return null;
    }

```
1、判断key值是否为null,在table[0]的链表中寻找;

2、计算hash值，进而获得table中的index值

3、在table[index]的链表中寻找，根据hash值和equals()方法获得相应的value。
 
 