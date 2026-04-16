using System;

namespace PropertyTest {
    class PropertyTest {
        private int _value;

        public PropertyTest() {
            _value = 42;
        }

        public int Value {
            get { return _value; }
            set { _value = value; }
        }

        public static void Main(string[] args) {
            PropertyTest obj = new PropertyTest();
            Console.WriteLine(obj.Value);
            obj.Value = 99;
            Console.WriteLine(obj.Value);
        }
    }
}
