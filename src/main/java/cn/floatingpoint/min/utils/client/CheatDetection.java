package cn.floatingpoint.min.utils.client;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 11:19:07
 */
public class CheatDetection {
    public int reach, sprint, noSlow;
    public int reachPercentage, sprintPercentage, noSlowPercentage;
    public boolean hacks = false;

    public CheatDetection() {
        reach = 0;
        reachPercentage = 0;
        sprint = 0;
        sprintPercentage = 0;
        noSlow = 0;
        noSlowPercentage = 0;
    }
}
