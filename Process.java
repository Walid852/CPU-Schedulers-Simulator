package CPU_Simulator;
public class Process {
    public  String name;
    public  int arrival;
    public  int burst;
    public  int completion;
    public  int turnaround;
    public  int waited;
    public  int priority;
    public  int starve;
    private int quantum_time;
    int waiting_time;
    int turnaround_time;
    int indexforall;
    int stopped_time;
    
    Process()
    {
        name="";
        waited=0;
        turnaround=0;
        arrival=-1;
        burst=-1;
        completion=-1;
        starve=0;
    }
    
    public void setTurnaround() {
        turnaround = completion-arrival;
    }
    @Override
    public String toString() {
        return String.valueOf(getName() + " : " + getPriority());
    }

    public Process(String name, int burst_time, int arrival_time, int priority) {
        this.name = name;
        this.burst = burst_time;
        this.arrival = arrival_time;
        this.setPriority(priority);
    }

    public Process(String name, int burst_time, int arrival_time, int priority, int quantum_time) {
        this.name = name;
        this.setBurst_time(burst_time);
        this.arrival = arrival_time;
        this.setPriority(priority);
        this.setQuantum_time(quantum_time);
        this.setTurnaround_Time(burst_time);
    }

    public String getName() {
        return name;
    }

    public int getBurst_time() {
        return burst;
    }

    public int getArrival_time() {
        return arrival;
    }

    public int getPriority() {
        return priority;
    }

    public int getQuantum_time() {
        return quantum_time;
    }

    public void setQuantum_time(int quantum_time) {
        this.quantum_time = quantum_time;
    }

    public void setBurst_time(int burst_time) {
        this.burst = burst_time;
    }

    public int getTurnaround_Time() {
        return turnaround_time;
    }

    public void setTurnaround_Time(int turnaround_Time) {
        this.turnaround_time = turnaround_Time;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}