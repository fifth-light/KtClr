using System;

namespace TypeTest {
    class Program {
        public static void Main(string[] args) {
            int[] arr = new int[3];
            arr[0] = 10;
            arr[1] = 20;
            arr[2] = 30;
            Console.WriteLine(arr[0] + arr[1] + arr[2]);

            object obj = 42;
            int unboxed = (int)obj;
            Console.WriteLine(unboxed);

            string str = "hello";
            object boxed = str;
            string casted = boxed as string;
            Console.WriteLine(casted);
        }
    }
}
