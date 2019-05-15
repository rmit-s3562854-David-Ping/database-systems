package org.rmit.student.tree;

import java.util.ArrayList;
import java.util.List;

public class ExternalMergeSort<K extends Comparable<K>, V> {

    public static void main(String[] args) {
        List<Integer> array = new ArrayList<>();
        array.add(6);
        array.add(3);
        array.add(2);
        array.add(5);
        array.add(4);
        array.add(1);

        ExternalMergeSort<Integer, int[]> externalMergeSort = new ExternalMergeSort();

        externalMergeSort.sort(array);

        for(int x : array) {
            System.out.println(x);
        }
    }

    /**
     * Sort array.
     *
     * @param array Array to be sorted.
     */
    public void sort(List<K> array) {

        // we only merge sort if length of array is greater than 1
        if (array.size() > 1) {
            int mid = (int) Math.floor(array.size() / 2);

            // Create new sub-arrays & copy elements array into the left and rigth sub-arrays
            List<K> left = new ArrayList<>(array.subList(0, mid));
            List<K> right = new ArrayList<>(array.subList(mid, array.size()));

            // sort the sub-arrays
            sort(left);
            sort(right);

            merge(left, right, array);
        }
    } // end of sort()


    /**
     * Merge left and right into array.
     *
     * @param left Left sub-array.
     * @param right Right sub-array.
     * @param array Merged array.
     */
    protected void merge(List<K> left, List<K> right, List<K> array) {
        int i = 0;
        int j = 0;
        int k = 0;

        // while there are elements in both left and right to be merged
        while (i < left.size() && j < right.size()) {
            if(left.get(i).compareTo(right.get(j)) < 0) {
                array.set(k, left.get(i));
                i++;
            } else {
                array.set(k, right.get(j));
                j++;
            }
            k++;
        }

        // if one of the sub-arrays is exhausted append the rest of the other one to array
        if (i == left.size()) {
            while (j < right.size()) {
                array.set(k, right.get(j));
                k++;
                j++;
            }
        } else if (j == right.size()) {
            while (i < left.size()) {
                array.set(k, left.get(i));
                k++;
                i++;
            }
        }
    }
}
