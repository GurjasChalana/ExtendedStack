package comp2402a4;

import com.sun.jdi.IntegerValue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.*;

public class FastSparrow implements RevengeOfSparrow {
  class ds {
    public int max;
    public long sum;

    ds(int m, long s) {
      max = m;
      sum = s;
    }
  }

  Stack<Integer> dsStack;
  int heightCtr;
  int ctr;
  int newBuildIndex;
  int indexAddedToo;
  ArrayList<ds> trees;

  public FastSparrow() {
    dsStack = new Stack<>();
    ctr = 0;
    heightCtr = 1;
    newBuildIndex = 5;
    indexAddedToo = 0;
    trees = new ArrayList<>();
  }

  public void push(int x) {
    System.out.println("[");
    for (int i = 0; i < trees.size();i++) {
      if (trees.get(i) != null) {
        System.out.print(trees.get(i).sum);
      }
      else {
        System.out.print("null");
      }
      System.out.print(",");
    }
    System.out.println("]");
    // FIX PUSH

    boolean firstIteration = false;

    // push into stack, if its an odd number of leaves then push 0 with it dont increment counter yet, otherwise set the element at the 0 to it
    dsStack.push(x);
    if (trees.isEmpty()) {
      trees.add(new ds(x, x));
      firstIteration = true;
      ctr++;
      indexAddedToo = 1;
    }

    // BUILD FUNCTIONALITY, IF THE TREE IS INCOMPLETE, COMPLETE IT WITH A BUNCH OF ZEROS
    // fix this,
    if (dsStack.size() > (1<<heightCtr-1) || firstIteration) {
      heightCtr++;
      trees.add(0, null);
      int index = 2;
      int tempHeight = 2;


      while (tempHeight <= heightCtr) {
        // ALWAYS FIVE BECAUSE WE ALREADY ADDED A 0 ABOVE, MEANING THIS IS THE BEGGINING INDEX OF THE ZEROS WE ADD
        for (int i = 0; i < (1<<tempHeight-1) / 2; i++) {
          // adds nulls as placeholders
          trees.add(index + i, null);

        }
        index = 2 * index + 1;
        tempHeight++;
      }
      // SET THE VALUE AT THE BUILD INDEX TO THE new VALUE TO MAINTAIN THE ORDER OF THE STACk
      if (!firstIteration) {
        trees.set(newBuildIndex, new ds(x, x));
        ctr++;
        indexAddedToo = newBuildIndex;
        newBuildIndex = 2 * newBuildIndex + 1;
      }
      // GLOBAL VARIABLE, every time we make a new build the place the value will be held is the first null or this index
    }


    // SETS THE ELEMENT TO THE NEXT OF IT TRYING TO FILL ALL THE NULLS, UNTIL ITS BUILDED AGAIN
    else {
      indexAddedToo = (1<<heightCtr-1) - 1 + ctr;
      trees.set(indexAddedToo, new ds(x, x));
      ctr++;
    }

    // AFTER EVEYRTHING IS CREATED, WE WANT TO MAKE A SINGLE WALK UP AND COMPARE WITH ITS LEFT OR RIGHT CHILDREN AND SET THE MAX
    // current index we start is of the element we just added, we make walk up

    makeWalk(indexAddedToo);

    // PRINTING THE MAX ARRAY
  }

  public Integer pop() {
    int temp = dsStack.pop();
    // pop from stack

    // get the previous element, there and return it, then set it to 0 as placeholder

    trees.set(indexAddedToo, null);

    // make a walk up and update parents
    makeWalk(indexAddedToo);
    // WHERE DO I DECREMENT HEIGHT, AND HOW TO GET RID OF 0 PLACEHOLDERS
    indexAddedToo--;
    ctr--;


    return temp;
  }

  public Integer get(int i) {
    if (i < 0 || i > size()) return null;
    return dsStack.get(i);
  }

  public Integer set(int i, int x) {
    if (i < 0 || i > size()) return null;
    // return the previous value from the stack, to set
    int temp = dsStack.get(i);
    dsStack.set(i, x);
    // INDEX IN ARRAY THAT WE WANT TO SET, basically beggining of our stack in our array plus the index they want
    int indexInArray = (1<<heightCtr-1) - 1 + i;

    // index starts off at this in our max array


    trees.set(indexInArray, new ds(x, x));

    // make a walk up and update parents
    makeWalk(indexInArray);

    return temp;
  }

  public Integer max() {
    if (dsStack.isEmpty()) {
      return null;
    } else {
      return trees.get(0).max;
    }
  }

  public long ksum(int k) {
    // go until this

    // last index of stack elements - k, gives you the rest of the elements so we can do this minus total sum to get the remaining elements
    int begginingIndex = indexAddedToo - k + 1;

    // height of subtree to travel
    int tempCtr = 1;
    long kSum;
    long totalMinus = 0;
    if (dsStack.isEmpty()) {
      return 0;
    }
    if (k <= 0) {
      return 0;
    }

    int value = 1;
    int height = 1;
    int x = k - 1 + (1<<heightCtr-1) - dsStack.size();

    while (value <= x)
    {
      height++;
      value = value << 1;
    }

    if (k >= dsStack.size()) {
      return trees.get(0).sum; // check here
    }
    if (k == 1) {
      return dsStack.get(dsStack.size() - 1); // check here
    } else {
      int currParent;
      currParent = getParent(begginingIndex);

      while (tempCtr < height) {
        // ur coming from the left, and the right exists add it to the total minus
        if (trees.get(getRightChild(currParent)) != null) {
          if (trees.get(getRightChild(currParent)).equals(trees.get(begginingIndex))) { // check here
            if (trees.get(getLeftChild(currParent)) != null) {
              totalMinus += trees.get(getLeftChild(currParent)).sum;
            }
          }
        }

        begginingIndex = currParent;
        currParent = (currParent - 1) >> 1;
        tempCtr++;
      }

      kSum = trees.get(begginingIndex).sum - totalMinus; // check here
    }

    return kSum;
  }

  public int size() {
    return dsStack.size();
  }

  public Iterator<Integer> iterator() {
    return dsStack.iterator();
  }

  public void makeWalk(int index) {
    int currParent;
    int walk = heightCtr;
    currParent = getParent(index);

    // use while loop to get to the top of the tree, update each parent along the way and then make parent equal to the new parent
    while (walk > 1) {
      // int max = Math.max((dsMax.get(2*currParent + 1)),dsMax.get(2*currParent + 2));
      if (trees.get(getLeftChild(currParent)) != null && trees.get(getRightChild(currParent)) != null) {
        int max = Math.max((trees.get(getLeftChild(currParent)).max), trees.get(getRightChild(currParent)).max);
        long sum = (trees.get(getLeftChild(currParent)).sum + trees.get(getRightChild(currParent)).sum);
        trees.set(currParent, new ds(max, sum));
      } else if (trees.get(getLeftChild(currParent)) != null) {
        trees.set(currParent, new ds(trees.get(getLeftChild(currParent)).max, trees.get(getLeftChild(currParent)).sum));
      } else if (trees.get(getRightChild(currParent)) != null) {
        trees.set(currParent, new ds(trees.get(getRightChild(currParent)).max, trees.get(getRightChild(currParent)).sum));
      } else {
        trees.set(currParent, null);
      }
      currParent = (currParent - 1) >> 1;
      walk--;
    }
  }

  public int getParent(int index) {
    return (index - 1) >> 1;
  }
  public int getLeftChild(int index) {
    return 2* index + 1;
  }
  public int getRightChild(int index) {
    return 2* index + 2;
  }




}




