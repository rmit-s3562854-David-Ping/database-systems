package org.rmit.student.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalMergeSort {

    public static void main(String[] args) {
        // Read the file you want to merge (give heap size as input argument)

        // Store the key inside an internal node (used to traverse the tree)
            // Each internal node contains an a

        // Store the data (the full byte array including the key) in a leaf node, has a key field as well

        List<Integer> numberList = new ArrayList<>();
        numberList.add(2);
        numberList.add(4);
        numberList.add(6);
        numberList.add(8);
        numberList.add(10);

        List<Integer> leftList = numberList.subList(0, numberList.size()/2);
        List<Integer> rightList = numberList.subList(numberList.size()/2, numberList.size());
//        int index = Collections.binarySearch(numberList, 5);
//        System.out.println(index);
//        int insertIndex = Math.abs(index + 1);

//        numberList.add(insertIndex, 5);

        for(int x: leftList) {
            System.out.println(x);
        }
        for(int x: rightList) {
            System.out.println(x);
        }
    }
}
