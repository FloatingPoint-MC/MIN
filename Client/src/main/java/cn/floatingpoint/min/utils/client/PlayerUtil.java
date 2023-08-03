package cn.floatingpoint.min.utils.client;

import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-03 11:30:19
 */
public class PlayerUtil {
    public static UUID formUUID(String raw) {
        if (raw.contains("-")) {
            return UUID.fromString(raw);
        } else {
            StringBuilder uuidBuilder = new StringBuilder();
            for (int i = 0; i < raw.length(); i++) {
                uuidBuilder.append(raw.charAt(i));
                if (i == 7 || i == 11 || i == 15 || i == 19) {
                    uuidBuilder.append("-");
                }
            }
            return UUID.fromString(uuidBuilder.toString());
        }
    }
}
