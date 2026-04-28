using System;
using System.Runtime.InteropServices;

namespace AsmNet
{
    class PinvokeTest
    {
        [DllImport("kernel32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern uint GetCurrentProcessId();

        public static void Main(string[] args)
        {
            uint pid1 = GetCurrentProcessId();
            uint pid2 = GetCurrentProcessId();
            if (pid1 != 0 && pid2 != 0 && pid1 == pid2)
            {
                Console.WriteLine("PASS");
            }
            else
            {
                Console.WriteLine("FAIL");
            }
        }
    }
}
