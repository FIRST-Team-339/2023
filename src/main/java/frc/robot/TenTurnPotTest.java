package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import frc.Hardware.Hardware;

public class TenTurnPotTest
    {

    public static void periodic()
    {
        double TenTime;
        TenTime = Hardware.tenPot.get(0, 3600);
        Timer.delay(TenTime);

    }
    }
