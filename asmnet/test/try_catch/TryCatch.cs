using System;

namespace AsmNet {
    class TryCatchTest {
        public static void Main(string[] args) {
            try {
                throw new Exception();
            } catch (Exception) {
                Console.WriteLine("Caught!");
            }
            Console.WriteLine("Done");
        }
    }
}
