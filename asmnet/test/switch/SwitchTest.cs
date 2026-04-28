using System;

namespace SwitchTest {
    class Program {
        public static void Main(string[] args) {
            int x = 2;
            switch (x) {
                case 0:
                    Console.WriteLine("zero");
                    break;
                case 1:
                    Console.WriteLine("one");
                    break;
                case 2:
                    Console.WriteLine("two");
                    break;
                default:
                    Console.WriteLine("other");
                    break;
            }
        }
    }
}
