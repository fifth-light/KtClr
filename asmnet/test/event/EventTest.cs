using System;

namespace EventTest {
    class EventTest {
        public event EventHandler TimeUp;

        public static void Main(string[] args) {
            EventTest obj = new EventTest();
            int count = 0;
            obj.TimeUp += (sender, e) => {
                count++;
                Console.WriteLine("Event fired!");
            };
            obj.TimeUp += (sender, e) => {
                count++;
                Console.WriteLine("Another handler!");
            };
            obj.TimeUp?.Invoke(obj, EventArgs.Empty);
            obj.TimeUp?.Invoke(obj, EventArgs.Empty);
            obj.TimeUp?.Invoke(obj, EventArgs.Empty);
            Console.WriteLine(count);
        }
    }
}
