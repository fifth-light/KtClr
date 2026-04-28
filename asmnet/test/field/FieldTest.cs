using System;

namespace FieldTest {
    class FieldTest {
        private int instanceField;
        public static int staticField;

        public FieldTest() {
            instanceField = 10;
        }

        public static void Main(string[] args) {
            FieldTest obj = new FieldTest();
            staticField = obj.instanceField;
            Console.WriteLine(staticField);
        }
    }
}
