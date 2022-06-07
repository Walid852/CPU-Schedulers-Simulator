package CPU_Simulator;
public class CPU {
    CPU()
    {
        Timer(0);
        this.CurrentP=new Process();
    }
    public Process CurrentP;
    public  int Timer;
    public void CurrentP(Process currentP) {
        CurrentP = currentP;
    }
    public void Timer(int timer) {
        Timer = timer;
    }
}