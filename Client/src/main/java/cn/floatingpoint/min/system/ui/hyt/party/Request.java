package cn.floatingpoint.min.system.ui.hyt.party;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-30 18:36:18
 */
public class Request {
    private final String name;
    private final String acceptId;
    private final String denyId;

    public Request(String name, String acceptId, String denyId) {
        this.name = name;
        this.acceptId = acceptId;
        this.denyId = denyId;
    }

    public String getName() {
        return name;
    }

    public String getAcceptId() {
        return acceptId;
    }

    public String getDenyId() {
        return denyId;
    }
}
