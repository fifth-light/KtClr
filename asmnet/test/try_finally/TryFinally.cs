using System;

namespace AsmNet {
    class TryFinallyTest {
        public static void Main(string[] args) {
            try {
                Console.WriteLine("Try");
            } finally {
                Console.WriteLine("Finally ran");
            }
            Console.WriteLine("Done");
        }
    }
}
