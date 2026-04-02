import java.awt.Color;

public class SortingAlgorithms {

    private static void pause(VisualPanel panel) throws InterruptedException {
        panel.repaint();
        Thread.sleep(panel.speed);
        panel.updateStats();
    }

    // Bubble Sort - Simple comparison-based algorithm
    public static void bubbleSort(int[] arr, VisualPanel panel) throws InterruptedException {
        panel.resetStats();
        
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            
            for (int j = 0; j < arr.length - i - 1; j++) {
                panel.resetColors();
                panel.colors[j] = new Color(231, 76, 60);        // Red - comparing
                panel.colors[j + 1] = new Color(231, 76, 60);
                panel.comparisons++;
                
                pause(panel);
                
                if (arr[j] > arr[j + 1]) {
                    // Swap elements
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    
                    panel.colors[j] = new Color(241, 196, 15);    // Yellow - swapped
                    panel.colors[j + 1] = new Color(241, 196, 15);
                    panel.swaps++;
                    swapped = true;
                    
                    pause(panel);
                }
            }
            
            panel.markSorted(arr.length - 1 - i);
            
            // Early termination if no swaps occurred
            if (!swapped) break;
        }
        
        // Mark all as sorted
        for (int k = 0; k < arr.length; k++) {
            panel.markSorted(k);
            if (k % 5 == 0) pause(panel);
        }
        panel.repaint();
    }

    // Selection Sort - Find minimum and place at beginning
    public static void selectionSort(int[] arr, VisualPanel panel) throws InterruptedException {
        panel.resetStats();
        
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            
            for (int j = i + 1; j < arr.length; j++) {
                panel.resetColors();
                panel.colors[minIndex] = new Color(46, 204, 113); // Green - current minimum
                panel.colors[j] = new Color(231, 76, 60);         // Red - comparing
                panel.colors[i] = new Color(52, 152, 219);        // Blue - current position
                panel.comparisons++;
                
                pause(panel);
                
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // Swap minimum with current position
            if (minIndex != i) {
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
                
                panel.colors[i] = new Color(241, 196, 15);      // Yellow - swapped
                panel.colors[minIndex] = new Color(241, 196, 15);
                panel.swaps++;
                pause(panel);
            }
            
            panel.markSorted(i);
        }
        
        // Mark all as sorted
        for (int k = 0; k < arr.length; k++) {
            panel.markSorted(k);
            if (k % 5 == 0) pause(panel);
        }
        panel.repaint();
    }

    // Insertion Sort - Insert each element into sorted portion
    public static void insertionSort(int[] arr, VisualPanel panel) throws InterruptedException {
        panel.resetStats();
        panel.markSorted(0); // First element is already "sorted"
        
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            
            panel.resetColors();
            panel.colors[i] = new Color(46, 204, 113); // Green - key element
            pause(panel);
            
            while (j >= 0 && arr[j] > key) {
                panel.resetColors();
                panel.colors[j] = new Color(231, 76, 60);         // Red - comparing
                panel.colors[j + 1] = new Color(241, 196, 15);    // Yellow - shifting
                panel.comparisons++;
                
                arr[j + 1] = arr[j];
                panel.swaps++;
                
                pause(panel);
                j--;
            }
            
            arr[j + 1] = key;
            panel.colors[j + 1] = new Color(241, 196, 15);
            panel.markSorted(j + 1);
            pause(panel);
        }
        
        // Mark all as sorted
        for (int k = 0; k < arr.length; k++) {
            panel.markSorted(k);
            if (k % 5 == 0) pause(panel);
        }
        panel.repaint();
    }

    // Merge Sort - Divide and conquer algorithm
    public static void mergeSort(int[] arr, int left, int right, VisualPanel panel) throws InterruptedException {
        if (left == 0 && right == arr.length - 1) {
            panel.resetStats();
        }
        
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSort(arr, left, mid, panel);
            mergeSort(arr, mid + 1, right, panel);
            merge(arr, left, mid, right, panel);
        }
        
        // Final pass to mark all as sorted
        if (left == 0 && right == arr.length - 1) {
            for (int k = 0; k < arr.length; k++) {
                panel.markSorted(k);
                if (k % 5 == 0) pause(panel);
            }
            panel.repaint();
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, VisualPanel panel) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        for (int i = 0; i < n1; i++) leftArray[i] = arr[left + i];
        for (int j = 0; j < n2; j++) rightArray[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            panel.resetColors();
            panel.colors[k] = new Color(241, 196, 15); // Yellow - merging
            panel.comparisons++;
            
            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                j++;
            }
            
            panel.swaps++;
            pause(panel);
            k++;
        }

        while (i < n1) {
            panel.resetColors();
            panel.colors[k] = new Color(241, 196, 15);
            arr[k] = leftArray[i];
            panel.swaps++;
            pause(panel);
            i++;
            k++;
        }

        while (j < n2) {
            panel.resetColors();
            panel.colors[k] = new Color(241, 196, 15);
            arr[k] = rightArray[j];
            panel.swaps++;
            pause(panel);
            j++;
            k++;
        }
        
        panel.resetColors();
    }

    // Quick Sort - Efficient divide and conquer algorithm
    public static void quickSort(int[] arr, int low, int high, VisualPanel panel) throws InterruptedException {
        if (low == 0 && high == arr.length - 1) {
            panel.resetStats();
        }
        
        if (low < high) {
            int pivotIndex = partition(arr, low, high, panel);
            quickSort(arr, low, pivotIndex - 1, panel);
            quickSort(arr, pivotIndex + 1, high, panel);
        }
        
        // Final pass to mark all as sorted
        if (low == 0 && high == arr.length - 1) {
            for (int k = 0; k < arr.length; k++) {
                panel.markSorted(k);
                if (k % 5 == 0) pause(panel);
            }
            panel.repaint();
        }
    }

    private static int partition(int[] arr, int low, int high, VisualPanel panel) throws InterruptedException {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            panel.resetColors();
            panel.colors[j] = new Color(231, 76, 60);         // Red - comparing
            panel.colors[high] = new Color(46, 204, 113);     // Green - pivot
            
            if (i >= 0) {
                panel.colors[i] = new Color(52, 152, 219);    // Blue - partition point
            }
            
            panel.comparisons++;
            pause(panel);
            
            if (arr[j] < pivot) {
                i++;
                
                // Swap elements
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                
                panel.colors[i] = new Color(241, 196, 15);    // Yellow - swapped
                panel.colors[j] = new Color(241, 196, 15);
                panel.swaps++;
                pause(panel);
            }
        }
        
        // Place pivot in correct position
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        panel.colors[i + 1] = new Color(241, 196, 15);
        panel.colors[high] = new Color(241, 196, 15);
        panel.swaps++;
        pause(panel);
        
        panel.markSorted(i + 1);
        return i + 1;
    }
}