using System;

namespace AsmNet {
    class TryFilterTest {
        public static void Main(string[] args) {
            try {
                throw new Exception();
            } catch (Exception) when (true) {
                Console.WriteLine("Filtered!");
            }
            Console.WriteLine("Done");
        }
    }
}
