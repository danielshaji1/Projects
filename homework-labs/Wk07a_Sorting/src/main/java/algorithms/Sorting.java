package algorithms;

public class Sorting {
    public int[] bubble(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                print(arr, i, j);
                if (arr[j] > arr[j+1])
                {
                    // swap temp and arr[i]
                    System.out.println("\t\tSwapping: " + arr[j] + " " + arr[j+1]);
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return arr;
    }

    public int[] selection(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            int mininum_index = i;
            for (int j = i+1; j < n; j++) {
                print(arr, i, j);
                // search for 'index' contains smallest number
                if (arr[j] < arr[mininum_index])
                    mininum_index = j;
            }
            // found the smallest now we do the swap.
            // Swap: temp (minimum index) and arr[i]
            System.out.println("\t\tSwapping: " + arr[mininum_index] + " " + arr[i]);
            int temp = arr[mininum_index];
            arr[mininum_index] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    public int[] insertion(int arr[])
    {
        // Split the array  virtually into two parts:
        //  *  a sorted part and (the ones we inspected)
        //  *  an unsorted part.
        // Values from the unsorted part are picked and placed in the correct position
        // in the sorted part.
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1; //


            // insert a 'card' into the left of the 'hand'
            // from the right, into the proper spot
            //   --> we do this by swapping.
            // Left array is kept sorted.
            /// so we continuously swap until
            // candidate is interted into its proper spot.
            while (
                    j >= 0
                    && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    public int[] mergeSortRecurse(int arr[], int l, int r)
    {
        System.out.print("splitting [l, r]: [" + l + ", " + r + "] Array: ");
        printArray(arr, l, r);

        if (l < r)
        {
            // Find the middle point when splitting
            int m = (l+r)/2;

            // Sort first and second halves
            mergeSortRecurse(arr, l, m);
            mergeSortRecurse(arr , m+1, r);

            // Merge the sorted halves
            // into an array arr[ l ... r ]
            merge(arr, l, m, r);
        }
        return arr;
    }

    // Merges two sub-arrays of arr[]. m - middle in left array
    //  1st (l-left)    subarray is arr[l ... m]
    //  2nd (r-right)   subarray is arr[m+1... r]
    // l - left, m - middle, r - right
    public void merge(int arr[], int l, int m, int r)
    {
        System.out.println("merge l m r: " + l + " " + m + " " + r);

        // Find sizes of two 'smaller' sub-arrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;         // r - ((m-1)+1)

        /* Create temp arrays with sizes from above */
        int L[] = new int [n1];
        int R[] = new int [n2];

        /* Copy data to temp arrays */
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];


        /* Merge the TWO temp arrays into arr[] */
        // Initial indexes of first and second sub arrays
        int i = 0, j = 0;

        // Keep indexes of
        // k for index in merged array
        // i for left array, j for right array
        int k = l;
        while (i < n1 && j < n2) // work left/right array indexes
        {
            // put 'smallest' into the merged array arr[]
            if (L[i] <= R[j])
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
            k++; // work next index in the merged array arr[]
        }
        // done merging, check for left over in either left or or array

        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            arr[k] = R[j];
            j++;
            k++;
        }

        System.out.print("After merge: ");
        printArray( arr, l, r );
    }


    // Merge sort with auxiliary array in parameters
    // It simplifies the merge process by avoiding the creation and
    // copying of temporary arrays within the merge method.
    // The auxiliary array (aux)  holds a copy of the elements
    // being sorted during the merge step.
    public int[] MergeSortIntAux(int arr[]) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }

        int[] aux = new int[arr.length];
        mergeSortAux(arr, aux, 0, arr.length - 1);
        return arr;
    }

    private void mergeSortAux(int[] arr, int[] aux, int low, int high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergeSortAux(arr, aux, low, mid);
            mergeSortAux(arr, aux, mid + 1, high);
            mergeAux(arr, aux, low, mid, high);
        }
    }

    private void mergeAux(int[] arr, int[] aux, int low, int mid, int high) {
        for (int k = low; k <= high; k++) {
            aux[k] = arr[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                arr[k] = aux[j++];
            } else if (j > high) {
                arr[k] = aux[i++];
            } else if (aux[i] <= aux[j]) {
                arr[k] = aux[i++];
            } else {
                arr[k] = aux[j++];
            }
        }
    }


    static void printArray(int arr[], int l, int r)
    {
        int n = arr.length;
        if( r >= n ) r = n-1;
        System.out.print("[ ");
        for (int i=l; i<r; ++i)
            System.out.print(arr[i] + ", ");
        System.out.println(arr[r] + " ]");
        // System.out.println("]");
    }
    public void print( int[] a, int i, int j ) {
        System.out.print( " i=" + i + " j=" + j + " " + "array: " );
        for(int k = 0; k < a.length; k++) {
            System.out.print(+ a[k] + " " );
        }
        System.out.print("\n");
    }
}