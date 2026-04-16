using System;

namespace JumpTest {
    class Program {
        public static void Main(string[] args) {
            int sum = 0;
            for (int i = 1; i <= 5; i++) {
                sum += i;
            }
            Console.WriteLine(sum);

            int x = 10;
            if (x > 5) {
                Console.WriteLine("big");
            } else {
                Console.WriteLine("small");
            }
        }
    }
}
