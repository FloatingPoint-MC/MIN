package cn.floatingpoint.min.utils.client;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-22 22:28:06
 */
public class Rank {
    private final int rank;
    private final double kd;

    public Rank(int rank, double kd) {
        this.rank = rank;
        this.kd = kd;
    }

    public int getRank() {
        return rank;
    }

    public double getKd() {
        return kd;
    }
}
