using System;
using System.Runtime.InteropServices;
using System.Text;

namespace AsmNet
{
    class Program
    {
        [StructLayout(LayoutKind.Sequential)]
        struct POINT
        {
            [MarshalAs(UnmanagedType.U4)]
            public int X;

            [MarshalAs(UnmanagedType.U4)]
            public int Y;
        }

        [return: MarshalAs(UnmanagedType.U4)]
        [DllImport("kernel32.dll")]
        static extern uint GetCurrentProcessId();

        [DllImport("kernel32.dll")]
        static extern uint GetCurrentDirectoryW(
            uint nBufferLength,
            [MarshalAs(UnmanagedType.LPWStr)] StringBuilder lpBuffer);

        [DllImport("user32.dll")]
        static extern bool GetCursorPos(ref POINT lpPoint);

        public static void Main(string[] args)
        {
            uint pid = GetCurrentProcessId();
            StringBuilder buf = new StringBuilder(260);
            uint len = GetCurrentDirectoryW(260, buf);
            POINT pt = new POINT();
            bool cursorOk = GetCursorPos(ref pt);
            if (pid > 0 && len > 0 && cursorOk)
                Console.WriteLine("PASS");
            else
                Console.WriteLine("FAIL");
        }
    }
}
