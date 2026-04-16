using System;

namespace LocalVariable {
    class Program {
        public static void Main(string[] args) {
            int x = 10;
            int y = 20;
            int sum = x + y;
            Console.WriteLine(sum);
            string msg = "Result: " + sum;
            Console.WriteLine(msg);
        }
    }
}
